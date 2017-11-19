package com.yyaammaa.sbh

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    companion object {
        private val KEY_PROJECT_NAME = "last_project_name"
    }

    private lateinit var projectName: EditText
    private lateinit var title: EditText
    private lateinit var body: EditText
    private lateinit var sendButton: Button

    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textFromIntent = ""
        if (Intent.ACTION_SEND.equals(intent.action)) {
            textFromIntent = intent.getStringExtra(Intent.EXTRA_TEXT)
        }

        setup(textFromIntent)
    }

    private fun setup(bodyText: String) {
        preference = PreferenceManager.getDefaultSharedPreferences(this)
        val lastProjectName = preference.getString(KEY_PROJECT_NAME, "")

        projectName = findViewById(R.id.act_main_project)
        projectName.setText(lastProjectName)
        projectName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                check()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        title = findViewById(R.id.act_main_title)
        title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                check()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        body = findViewById(R.id.act_main_body)
        if (bodyText.isNotEmpty()) {
            body.setText(bodyText)
        }
        body.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                check()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sendButton = findViewById(R.id.act_main_send)
        sendButton.setOnClickListener(this::onSendButtonClick)
    }

    private fun check() {
        val canSend = projectName.text.toString().isNotEmpty()
            && body.text.toString().isNotEmpty()
        sendButton.isEnabled = canSend
    }

    private fun onSendButtonClick(v: View) {
        preference
            .edit()
            .putString(KEY_PROJECT_NAME, projectName.text.toString())
            .apply()

        val url = "https://scrapbox.io" +
            "/${projectName.text}" +
            "/${if (title.text.isEmpty()) "new" else title.text}" +
            "?body=${body.text}"

        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        intent.launchUrl(this, Uri.parse(url))
    }
}
