package digital.steva.formumat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import digital.steva.formumat.helpers.Convert
import digital.steva.formumat.redux.ClearValue
import digital.steva.formumat.redux.Dispatcher
import digital.steva.formumat.redux.FormumatValues
import digital.steva.formumat.redux.SetValue
import digital.steva.formumat.schema.IntegerField
import digital.steva.formumat.schema.IntegerType
import digital.steva.formumat.schema.NumberField
import digital.steva.formumat.schema.NumberType
import digital.steva.formumat.schema.SliderField

@Composable
fun IntegerView(
    integerField: IntegerField,
    integerType: IntegerType?,
    values: FormumatValues,
    dispatch: Dispatcher,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val property = integerField.property?.eval(values) ?: ""
    val label = integerField.title.eval(values)
    val fieldEnabled = enabled && integerField.enabled.eval(values)
    var text = Convert.toString(values[property])
    var isValid = text.isBlank() || text.toIntOrNull() != null

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (it.isBlank()) {
                dispatch(ClearValue(property, values.listContext))
            } else {
                it.toIntOrNull()
                    ?.let { value -> dispatch(SetValue(property, value, values.listContext)) }
            }
        },
        label = { Text(label) },
        singleLine = true,
        enabled = fieldEnabled,
        isError = !isValid,
        supportingText = {
            if (!isValid) {
                Text("Bitte geben sie eine Ganzzahl ein.")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SliderView(
    sliderField: SliderField,
    integerType: IntegerType?,
    values: FormumatValues,
    dispatch: Dispatcher,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val value = values[sliderField.property?.eval(values)]
    val label = sliderField.title.eval(values)
    val fieldEnabled = enabled && sliderField.enabled.eval(values)

    Column(
        modifier = modifier
    ) {
        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Slider(
            value = ((value as? Int) ?: (value as? Double))?.toFloat() ?: 0f,
            onValueChange = {
                dispatch(
                    SetValue(
                        sliderField.property?.eval(values) ?: "",
                        it.toInt(),
                        values.listContext
                    )
                )
            },
            enabled = fieldEnabled,
            valueRange = sliderField.min.toFloat()..sliderField.max.toFloat()
        )
    }
}

@Composable
fun NumberView(
    numberField: NumberField,
    numberType: NumberType?,
    values: FormumatValues,
    dispatch: Dispatcher,
    enabled: Boolean,
    modifier: Modifier
) {
    val property = numberField.property?.eval(values) ?: ""
    val label = numberField.title.eval(values)
    val fieldEnabled = enabled && numberField.enabled.eval(values)
    var text = Convert.toString(values[property])
    var isValid = text.isBlank() || text.toDoubleOrNull() != null

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (it.isBlank()) {
                dispatch(ClearValue(property, values.listContext))
            } else {
                it.toDoubleOrNull()
                    ?.let { value -> dispatch(SetValue(property, value, values.listContext)) }
            }
        },
        label = { Text(label) },
        singleLine = true,
        enabled = fieldEnabled,
        isError = !isValid,
        supportingText = {
            if (!isValid) {
                Text("Bitte geben sie eine Fließkommazahl ein.")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}
