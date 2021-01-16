package com.thomaskuenneth.customcomponentdemo1

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RelativeLayout

class EditableCheckbox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val checkBox = CheckBox(context)
    private val resIdCheckBox = ResId@ 1

    private val editText = EditText(context)
    private val resIdEditText = ResId@ 2

    var text: String
        get() = editText.text.toString()
        set(value) {
            editText.setText(value)
        }

    var checked: Boolean
        get() = checkBox.isChecked
        set(value) {
            checkBox.isChecked = value
        }

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.EditableCheckbox
        )
        checkBox.isChecked = a.getBoolean(R.styleable.EditableCheckbox_checked, false)
        checkBox.id = resIdCheckBox
        editText.setText(a.getString(R.styleable.EditableCheckbox_text))
        editText.id = resIdEditText
        a.recycle()
        val paramsCheckBox = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        paramsCheckBox.addRule(ALIGN_PARENT_START, TRUE)
        paramsCheckBox.addRule(ALIGN_PARENT_TOP, TRUE)
        addView(checkBox, paramsCheckBox)
        val paramsEditText = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        paramsEditText.addRule(END_OF, checkBox.id)
        paramsEditText.addRule(ALIGN_BASELINE, checkBox.id)
        addView(editText, paramsEditText)
    }
}