package com.ecloud.apps.watermeterreader.feature.reader.reader.states

import com.ecloud.apps.watermeterreader.core.ui.TextFieldState
import com.ecloud.apps.watermeterreader.core.ui.textFieldStateSaver

class MeterNoState : TextFieldState()

val MeterNoStateSaver = textFieldStateSaver(MeterNoState())
