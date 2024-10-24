package com.example.sugarfree


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun challanges(navController: NavController) {
    // Remember scroll state for the column
    val scrollState = rememberScrollState()

    // Create a Column with vertical scrolling
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the available size
            .verticalScroll(scrollState) // Enable vertical scrolling
            .padding(16.dp) // Padding around the Column
    ) {
        // Section: BUILD YOUR FOUNDATIONS
        SectionHeader("Challenges")

        // Add individual CourseBox with clickable functionality
        CourseBox(
            imageId = R.drawable.image5,
            contentDescription = "Course 1",
            text = "",
            onClick = {
                navController.navigate("details1")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        CourseBox(
            imageId = R.drawable.image6,
            contentDescription = "Course 2",
            text = "",
            onClick = {
                navController.navigate("details2")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        CourseBox(
            imageId = R.drawable.image7,
            contentDescription = "Course 3",
            text = "",
            onClick = {
                navController.navigate("details3")
            }
        )

    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp) // Padding at the bottom of the header
    )
}

@Composable
fun CourseBox(imageId: Int, contentDescription: String, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the container
            .clip(RoundedCornerShape(8.dp)) // Rounded corners
            .background(Color.LightGray) // Background color
            .clickable(onClick = onClick) // Clickable modifier with provided lambda
            .padding(16.dp) // Padding around the content
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display an image on the left side
            Image(
                painter = painterResource(id = imageId),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(300.dp) // Adjust the size as needed
                    .clip(RoundedCornerShape(8.dp)) // Optional: rounded corners
            )

            Spacer(modifier = Modifier.width(16.dp)) // Spacing between image and text

            // Display text on the right side
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}