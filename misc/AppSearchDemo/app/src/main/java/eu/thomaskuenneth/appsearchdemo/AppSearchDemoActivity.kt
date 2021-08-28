package eu.thomaskuenneth.appsearchdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appsearch.annotation.Document
import androidx.appsearch.app.*
import androidx.appsearch.exceptions.AppSearchException
import androidx.appsearch.localstorage.LocalStorage
import androidx.appsearch.platformstorage.PlatformStorage
import androidx.core.os.BuildCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import eu.thomaskuenneth.appsearchdemo.databinding.MainBinding
import java.util.*

private const val TAG = "AppSearchDemoActivity"
private const val DATABASE_NAME = "appsearchdemo"

class AppSearchObserver(private val context: Context) : LifecycleObserver {

    lateinit var sessionFuture: ListenableFuture<AppSearchSession>

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun setupAppSearch() {
        sessionFuture = if (BuildCompat.isAtLeastS()) {
            PlatformStorage.createSearchSession(
                PlatformStorage.SearchContext.Builder(context, DATABASE_NAME)
                    .build()
            )
        } else {
            LocalStorage.createSearchSession(
                LocalStorage.SearchContext.Builder(context, DATABASE_NAME)
                    .build()
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun teardownAppSearch() {
        /* val closeFuture = */ Futures.transform<AppSearchSession, Unit>(
            sessionFuture,
            { session ->
                session?.close()
                Unit
            }, context.mainExecutor
        )
    }
}

class AppSearchDemoActivity : AppCompatActivity() {

    private lateinit var appSearchObserver: AppSearchObserver
    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appSearchObserver = AppSearchObserver(applicationContext)
        lifecycle.addObserver(appSearchObserver)
        lifecycleScope.launchWhenResumed {
            setSchema()
            addDocument()
            search()
            persist()
        }
    }

    private fun setSchema() {
        val setSchemaRequest =
            SetSchemaRequest.Builder().addDocumentClasses(MyDocument::class.java)
                .build()
        /* val setSchemaFuture = */ Futures.transformAsync(
            appSearchObserver.sessionFuture,
            { session ->
                session?.setSchema(setSchemaRequest)
            }, mainExecutor
        )
    }

    private fun addDocument() {
        val doc = MyDocument(
            namespace = packageName,
            id = UUID.randomUUID().toString(),
            score = 10,
            message = "Hello, this doc was created ${Date()}"
        )
        val putRequest = PutDocumentsRequest.Builder().addDocuments(doc).build()
        val putFuture = Futures.transformAsync(
            appSearchObserver.sessionFuture,
            { session ->
                session?.put(putRequest)
            }, mainExecutor
        )
        Futures.addCallback(
            putFuture,
            object : FutureCallback<AppSearchBatchResult<String, Void>?> {
                override fun onSuccess(result: AppSearchBatchResult<String, Void>?) {
                    output("successfulResults = ${result?.successes}")
                    output("failedResults = ${result?.failures}")
                }

                override fun onFailure(t: Throwable) {
                    output("Failed to put document(s).")
                    Log.e(TAG, "Failed to put document(s).", t)
                }
            },
            mainExecutor
        )
    }

    private fun search() {
        val searchSpec = SearchSpec.Builder()
            .addFilterNamespaces(packageName)
            .setResultCountPerPage(100)
            .build()
        val searchFuture = Futures.transform(
            appSearchObserver.sessionFuture,
            { session ->
                session?.search("hello", searchSpec)
            },
            mainExecutor
        )
        Futures.addCallback(
            searchFuture,
            object : FutureCallback<SearchResults> {
                override fun onSuccess(searchResults: SearchResults?) {
                    searchResults?.let {
                        iterateSearchResults(searchResults)
                    }
                }

                override fun onFailure(t: Throwable?) {
                    Log.e("TAG", "Failed to search in AppSearch.", t)
                }
            },
            mainExecutor
        )
    }

    private fun iterateSearchResults(searchResults: SearchResults) {
        Futures.transform(
            searchResults.nextPage,
            { page: List<SearchResult>? ->
                page?.forEach { current ->
                    val genericDocument: GenericDocument = current.genericDocument
                    val schemaType = genericDocument.schemaType
                    val document: MyDocument? = try {
                        if (schemaType == "MyDocument") {
                            genericDocument.toDocumentClass(MyDocument::class.java)
                        } else null
                    } catch (e: AppSearchException) {
                        Log.e(
                            TAG,
                            "Failed to convert GenericDocument to MyDocument",
                            e
                        )
                        null
                    }
                    output("Found ${document?.message}")
                }
            },
            mainExecutor
        )
    }

    private fun persist() {
        val requestFlushFuture = Futures.transformAsync(
            appSearchObserver.sessionFuture,
            { session -> session?.requestFlush() }, mainExecutor
        )
        Futures.addCallback(requestFlushFuture, object : FutureCallback<Void?> {
            override fun onSuccess(result: Void?) {
                // Success! Database updates have been persisted to disk.
            }

            override fun onFailure(t: Throwable) {
                Log.e(TAG, "Failed to flush database updates.", t)
            }
        }, mainExecutor)
    }

    private fun output(s: String) {
        binding.textview.append("$s\n")
    }
}

@Document
data class MyDocument(
    @Document.Namespace
    val namespace: String,

    @Document.Id
    val id: String,

    @Document.Score
    val score: Int,

    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    val message: String
)