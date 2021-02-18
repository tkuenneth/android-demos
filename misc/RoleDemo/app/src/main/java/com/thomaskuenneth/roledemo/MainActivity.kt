package com.thomaskuenneth.roledemo

import android.app.role.RoleManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.role.RoleManagerCompat

private const val REQUEST_ROLE_BROWSER = 123

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSystemService(RoleManager::class.java)?.run {
            if (isRoleAvailable(RoleManagerCompat.ROLE_BROWSER)) {
                if (!isRoleHeld(RoleManagerCompat.ROLE_BROWSER)) {
                    val intent = createRequestRoleIntent(RoleManagerCompat.ROLE_BROWSER)
                    startActivityForResult(intent, REQUEST_ROLE_BROWSER)
                } else
                    toast(R.string.has_role)
            } else toast(R.string.role_not_available)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ROLE_BROWSER && resultCode == RESULT_OK) {
            toast(R.string.role_acquired)
        }
    }

    private fun toast(resId: Int) {
        Toast.makeText(baseContext, resId, Toast.LENGTH_SHORT).show()
    }
}