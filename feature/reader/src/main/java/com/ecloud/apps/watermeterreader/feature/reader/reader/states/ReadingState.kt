package com.ecloud.apps.watermeterreader.feature.reader.reader.states

import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.textFieldStateSaver

class ReadingsState : TextFieldState()

val ReadingsStateSaver = textFieldStateSaver(ReadingsState())
