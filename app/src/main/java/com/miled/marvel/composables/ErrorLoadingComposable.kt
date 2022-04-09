package com.miled.marvel.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miled.marvel.R

@Composable
fun ErrorRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = stringResource(id = R.string.comic_error_icon_content_description),
            modifier = Modifier.size(40.dp)
        )
        Text(
            text,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}
@Preview(showSystemUi = true)
@Composable
private fun ErrorRowPreview() {
    ErrorRow(text = stringResource(id = R.string.errors_no_connexion))
}