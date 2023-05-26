package com.ecloud.apps.watermeterreader.feature.reader.states

import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.textFieldStateSaver

class RemarksState : TextFieldState()

val RemarksStateSaver = textFieldStateSaver(RemarksState())
