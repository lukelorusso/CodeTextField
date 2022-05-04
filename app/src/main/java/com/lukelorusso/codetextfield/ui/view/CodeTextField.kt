package com.lukelorusso.codetextfield.ui.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.lukelorusso.codetextfield.databinding.CodeTextFieldEditTextBinding
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


private val DEFAULT_CHAR_TEXT_MODIFIER = Modifier
    .padding(horizontal = 15.dp)
    .height(100.dp)
    .width(60.dp)
    .background(
        color = Color(0xFFC8E6FA),
        shape = RoundedCornerShape(4.dp)
    )

/**
 * @author Copyright (C) 2022 Luke Lorusso - https://lukelorusso.com
 * Licensed under the Apache License Version 2.0
 *
 * This [Composable] [Box] allows you to render a [Row] of [Text] which makes it easy to render and
 * input a [String] char by char: useful for OTPs, pins and passwords.
 *
 * @param modifier applied to the [Box]
 * @param enabled if false, the text cannot be changed
 * @param initialText is the [String] from where you want to start: should NOT change, unless you
 *  need to re-draw the UI
 * @param maxLength is the max length of the [String] to input
 * @param inputType allows you to chose the right [InputType] for the text to input
 * @param charEmptyPlaceholder is the char to be shown when no [Char] has been input yet
 * @param charMasker if not null, allows you to mash the input [Char] for privacy
 * @param charTextModifier is the customizable modifier for the [Text] rendering each [Char]
 * @param charTextCurrentModifier if not null, allows you to use a different custom modifier to
 *  render the current [Char] to input: it can act like a cursor
 * @param charTextStyle is the [TextStyle] to be used for the [Text] rendering each [Char]
 * @param scrollAnimationSpec allows you to specify a custom [AnimationSpec] for the horizontal
 *  scroll, which is used in case the [Row] cannot be entirely displayed on the screen
 * @param onTextChanged is the lambda callback that allows you to react to text changes: it returns
 *  two parameters:
 *  - a [String] which is the new value after a change
 *  - a [Boolean] which is true when the length of the new input [String] is equal to maxLength
 */
@Composable
fun CodeTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    initialText: String = "",
    maxLength: Int = 4,
    inputType: Int = InputType.TYPE_CLASS_TEXT,
    charEmptyPlaceholder: Char = ' ',
    charMasker: Char? = null, // if null, the characters will not be masked
    charTextModifier: Modifier = DEFAULT_CHAR_TEXT_MODIFIER,
    charTextCurrentModifier: Modifier? = DEFAULT_CHAR_TEXT_MODIFIER
        .drawBehind {
            drawLine(
                color = Color.Blue,
                start = Offset(x = 0f, y = size.height),
                end = Offset(size.width, y = size.height),
                strokeWidth = 4.dp.toPx()
            )
        }, // if null, charTextModifier will be used
    charTextStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 54.sp
    ),
    scrollAnimationSpec: AnimationSpec<Float> = SpringSpec(stiffness = Spring.StiffnessMediumLow),
    onTextChanged: (String, Boolean) -> Unit = { _, _ -> } // { code, isCompleted -> }
) {
    val editTextReference = remember { mutableStateOf<EditText?>(null) }
    val text = remember(
        initialText
            .toMutableList()
            ::toMutableStateList
    )
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            editable?.also {
                val newString = it.toString()
                text.clear()
                newString.map { char -> text.add(char) }
                onTextChanged(newString, newString.length == maxLength)
                editTextReference.value?.setSelection(newString.length)
            }
        }
    }
    val isEditTextFocused = remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()
    val layoutIntSize = remember { mutableStateOf(IntSize.Zero) }

    return Box(
        modifier = modifier
            .horizontalScroll(horizontalScrollState)
            .clickable {
                if (enabled) {
                    editTextReference.value?.focusOnLastLetter()
                }
            }
    ) {
        XmlView(
            enabled = enabled,
            initialText = initialText,
            maxLength = maxLength,
            inputType = inputType,
            isEditTextFocused = isEditTextFocused,
            textWatcher = textWatcher,
            layoutIntSize = layoutIntSize,
            editTextReference = editTextReference
        )

        CharRowView(
            maxLength = maxLength,
            mutableText = text,
            isEditTextFocused = isEditTextFocused.value,
            layoutIntSize = layoutIntSize,
            charEmptyPlaceholder = charEmptyPlaceholder,
            charMasker = charMasker,
            charTextModifier = charTextModifier,
            charTextCurrentModifier = charTextCurrentModifier,
            charTextStyle = charTextStyle,
            scrollAnimationSpec = scrollAnimationSpec,
            horizontalScrollState = horizontalScrollState
        )
    }
}

@Composable
private fun XmlView(
    enabled: Boolean,
    initialText: String,
    maxLength: Int,
    inputType: Int,
    isEditTextFocused: MutableState<Boolean>,
    layoutIntSize: MutableState<IntSize>,
    textWatcher: TextWatcher,
    editTextReference: MutableState<EditText?>
) = AndroidViewBinding(
    modifier = Modifier
        .then(
            with(LocalDensity.current) {
                Modifier.size(
                    width = layoutIntSize.value.width.toDp(),
                    height = layoutIntSize.value.height.toDp()
                )
            }
        ),
    factory = CodeTextFieldEditTextBinding::inflate,
) {
    if (editTextReal != editTextReference.value) {
        editTextReal.apply {
            setInputType(inputType)
            setText(initialText)
            isEnabled = enabled
            filters = arrayOf(InputFilter.LengthFilter(maxLength))
            addTextChangedListener(textWatcher)
            onFocusChangeListener = View.OnFocusChangeListener { _, isItFocused ->
                isEditTextFocused.value = isItFocused
            }
        }
        editTextReference.value?.removeTextChangedListener(textWatcher)
        editTextReference.value = editTextReal
    }
}

private fun EditText.focusOnLastLetter() {
    requestFocus()
    setSelection(text.length)
}

@Composable
@SuppressLint("ModifierParameter")
private fun CharRowView(
    maxLength: Int,
    mutableText: SnapshotStateList<Char>,
    isEditTextFocused: Boolean,
    layoutIntSize: MutableState<IntSize>,
    charEmptyPlaceholder: Char,
    charMasker: Char?,
    charTextModifier: Modifier,
    charTextCurrentModifier: Modifier?,
    charTextStyle: TextStyle,
    scrollAnimationSpec: AnimationSpec<Float>,
    horizontalScrollState: ScrollState
) = Row(
    modifier = Modifier
        .wrapContentHeight()
        .onSizeChanged { newSize -> layoutIntSize.value = newSize },
    horizontalArrangement = Arrangement.Center
) {
    val text = String(mutableText.toCharArray())
    val textLength = text.length
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalContext.current.resources.configuration

    for (i in 0 until maxLength) {
        val textModifier = if (i == textLength && isEditTextFocused)
            charTextCurrentModifier ?: charTextModifier
        else
            charTextModifier
        Text(
            modifier = textModifier
                .wrapContentHeight(align = Alignment.CenterVertically)
                .onGloballyPositioned { coordinates ->
                    val shouldScroll = (i == textLength || i == textLength - 1) &&
                            !coordinates.isFullyVisible(configuration)
                    if (shouldScroll) {
                        val actualScrollValue = horizontalScrollState.value
                        val newPosition = coordinates.positionInParent().x.roundToInt() -
                                coordinates.size.width
                        val decreaseCondition = newPosition < actualScrollValue
                                && actualScrollValue != 0
                        val increaseCondition = actualScrollValue < newPosition
                                && actualScrollValue != horizontalScrollState.maxValue
                        if (decreaseCondition || increaseCondition) coroutineScope.launch {
                            horizontalScrollState.animateScrollTo(
                                newPosition,
                                scrollAnimationSpec
                            )
                        }
                    }
                },
            text = (when {
                i < textLength && charMasker == null ->
                    text[i]
                i < textLength && charMasker != null ->
                    charMasker
                else ->
                    charEmptyPlaceholder
            }).toString(),
            textAlign = TextAlign.Center,
            style = charTextStyle
        )
    }
}

private fun LayoutCoordinates.isFullyVisible(configuration: Configuration): Boolean {
    val (width, height) = this.size
    val (x1, y1) = this.positionInRoot()
    val x2 = x1 + width
    val y2 = y1 + height
    val screenWidth = configuration.screenWidthDp.toPx
    val screenHeight = configuration.screenHeightDp.toPx
    return (x1 >= 0 && y1 >= 0) && (x2 <= screenWidth && y2 <= screenHeight)
}

private val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

@Preview
@Composable
private fun CodeTextFieldAlphanumericPreview() = CodeTextField(
    initialText = "a0z"
)

@Preview
@Composable
private fun CodeTextFieldNumberPreview() = CodeTextField(
    initialText = "123",
    inputType = InputType.TYPE_CLASS_NUMBER,
    charEmptyPlaceholder = '#'
)

@Preview
@Composable
private fun CodeTextFieldMaskerPreview() = CodeTextField(
    initialText = "aaa",
    charMasker = '•'
)
