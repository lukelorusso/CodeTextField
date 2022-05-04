package com.lukelorusso.codetextfield.ui.screen.main

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukelorusso.codetextfield.extension.hideKeyboard
import com.lukelorusso.codetextfield.ui.theme.AppTheme
import com.lukelorusso.codetextfield.ui.view.CodeTextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        CodeTextField(
                            modifier = Modifier
                                .padding(vertical = 60.dp),
                            inputType = InputType.TYPE_CLASS_NUMBER,
                            onTextChanged = ::onTextChanged
                        )
                    }
                )
            }
        }
    }

    private fun onTextChanged(code: String, isComplete: Boolean) {
        if (isComplete) {
            hideKeyboard()
            Toast.makeText(this, code, Toast.LENGTH_SHORT).show()
        }
    }
}
