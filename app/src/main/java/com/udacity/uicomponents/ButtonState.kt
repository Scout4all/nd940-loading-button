package com.udacity.uicomponents


sealed class ButtonState {
    object Initial : ButtonState()
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}