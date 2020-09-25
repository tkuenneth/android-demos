package com.thomaskuenneth.powermenudemo

import android.app.PendingIntent
import android.content.Intent
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.BooleanAction
import android.service.controls.actions.ControlAction
import android.service.controls.templates.ControlButton
import android.service.controls.templates.ControlTemplate
import android.service.controls.templates.ToggleTemplate
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.reactivestreams.FlowAdapters
import java.util.concurrent.Flow.Publisher
import java.util.function.Consumer

const val CONTROL_REQUEST_CODE = 123
const val MY_UNIQUE_DEVICE_ID = "123"
const val KEY_MESSAGE = "MyPowerService"
const val TEMPLATE_ID = "123"

class MyPowerService : ControlsProviderService() {

    private lateinit var replayProcessor: ReplayProcessor<Control>

    override fun createPublisherForAllAvailable(): Publisher<Control> {
        val control =
            Control.StatelessBuilder(MY_UNIQUE_DEVICE_ID, createPendingIntent())
                .setTitle(getString(R.string.title))
                .setSubtitle(getString(R.string.subtitle))
                .setStructure(getString(R.string.structure))
                .setDeviceType(DeviceTypes.TYPE_SWITCH)
                .build()
        return FlowAdapters.toFlowPublisher(Flowable.fromIterable(listOf(control)))
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Publisher<Control> {
        replayProcessor = ReplayProcessor.create()
        if (controlIds.contains(MY_UNIQUE_DEVICE_ID)) {
            val control =
                Control.StatefulBuilder(MY_UNIQUE_DEVICE_ID, createPendingIntent())
                    .setTitle(getString(R.string.title))
                    .setSubtitle(getString(R.string.subtitle))
                    .setStructure(getString(R.string.structure))
                    .setControlTemplate(
                        createControlTemplate(true)
                    )
                    .setDeviceType(DeviceTypes.TYPE_SWITCH)
                    .setStatus(Control.STATUS_OK)
                    .build()
            replayProcessor.onNext(control)
        }
        return FlowAdapters.toFlowPublisher(replayProcessor)
    }

    override fun performControlAction(
        controlId: String, action: ControlAction, consumer: Consumer<Int?>
    ) {
        if (action is BooleanAction) {
            consumer.accept(ControlAction.RESPONSE_OK)
            val control = Control.StatefulBuilder(MY_UNIQUE_DEVICE_ID, createPendingIntent())
                .setTitle(getString(R.string.title))
                .setSubtitle(getString(R.string.subtitle))
                .setStructure(getString(R.string.structure))
                .setControlTemplate(createControlTemplate(action.newState))
                .setDeviceType(DeviceTypes.TYPE_SWITCH)
                .setStatus(Control.STATUS_OK)
                .build()
            replayProcessor.onNext(control)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, PowerMenuDemoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(KEY_MESSAGE, getString(R.string.config))
        return PendingIntent.getActivity(
            this,
            CONTROL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createControlTemplate(isSelected: Boolean): ControlTemplate {
        return ToggleTemplate(
            TEMPLATE_ID,
            ControlButton(isSelected, getString(R.string.on_off))
        )
    }
}
