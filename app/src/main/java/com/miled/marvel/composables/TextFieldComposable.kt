package com.miled.marvel.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.miled.marvel.R

@Composable
fun TextFieldComposable(
    hint: String,
    text: String = "",
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit,
    onSearch: () -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        trailingIcon = {
            Row(modifier = Modifier.padding(end = 10.dp)) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.comics_screen_search_icon_description),
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable {
                            onSearch()
                        }
                )
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = stringResource(id = R.string.comics_screen_clear_icon_description),
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable {
                            onClearText()
                        }
                )
            }

        },
    )
}