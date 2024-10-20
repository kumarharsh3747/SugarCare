package com.example.sugarfree

import android.content.Intent
import android.net.Uri // Add this import for Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Symptoms() {
    val context = LocalContext.current // Get the context to use for launching intents

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Symptoms", fontSize = 40.sp,fontWeight = FontWeight.Bold ,color = MaterialTheme.colorScheme.onSecondary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0x7C0F0F3B)
                )
            )
        }
    ) { paddingValues ->
        // Add vertical scroll state to make the page scrollable
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(2.dp)
                .verticalScroll(scrollState)  // Make content scrollable
                .background(Color(0xFFE3D4EA)) // Set the background color of the main content
        ) {
            // Heading
            Text(text = "\nWhat is Diabetes?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Paragraph
            Text(
                text = "Diabetes is a chronic disease that occurs when the pancreas doesn't produce enough insulin or when the body can't effectively use the insulin it produces. Insulin is a hormone that regulates blood glucose. \n\nMostly, diabetes is genetic.",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Subheading1
            Text(text = "Why Monitor Symptoms?", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Keeping track of symptoms allows individuals to spot patterns and seek timely medical care. " +
                        "It can also assist healthcare providers in diagnosing and managing health conditions effectively.\n",
                fontSize = 18.sp
            )

            // Subheading 1.1
            Text(text = "Symptoms?", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            SymptomsTreeLayout()

            Spacer(modifier = Modifier.height(24.dp))

            // Heading2
            Text(text = "Types of Diabetes", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Subheading 2.1
            Text(text = "Type 1 Diabetes", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Type 1 diabetes is where your blood glucose (sugar) level is too high because your body can’t make a hormone called insulin. \n\n" +
                        "This happens because your body attacks the cells in your pancreas that make insulin, meaning you can’t produce any at all.",
                fontSize = 18.sp
            )

            // Subheading 2.2
            Text(text = "\nType 2 Diabetes", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "With type 2 diabetes the insulin your pancreas makes can’t work properly, or your pancreas can’t make enough insulin. This means your blood glucose (sugar) levels keep rising.  \n\n" +
                        "Around 90% of people with diabetes in the UK have type 2. It is serious condition and can be lifelong.",
                fontSize = 18.sp
            )

            // Subheading 2.3
            Text(text = "\nGestational diabetes", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gestational diabetes is diabetes that can develop during pregnancy. It affects women who haven't been affected by diabetes before. It means you have high blood sugar and need to take extra care of yourself and your bump. This will include eating well and keeping active.\n\n" +
                        "It usually goes away again after giving birth. It is usually diagnosed from a blood test 24 to 28 weeks into pregnancy.",
                fontSize = 18.sp
            )

            // Subheading 2.4
            Text(text = "\nSome other diabetes", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Clickable text for MODY
            ClickableText(
                text = AnnotatedString("Maturity onset diabetes of the young (MODY)\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/mody"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Neonatal diabetes
            ClickableText(
                text = AnnotatedString("Neonatal diabetes\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/neonatal-diabetes"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Wolfram Syndrome
            ClickableText(
                text = AnnotatedString("Wolfram Syndrome\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/wolfram-syndrome"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Alström Syndrome
            ClickableText(
                text = AnnotatedString("Alström Syndrome\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/alstrom-syndrome"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Latent Autoimmune diabetes in Adults (LADA)
            ClickableText(
                text = AnnotatedString("Latent Autoimmune diabetes in Adults (LADA)\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/latent-autoimmune-diabetes"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Type 3c diabetes
            ClickableText(
                text = AnnotatedString("Type 3c diabetes\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/type3c-diabetes"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Steroid-induced diabetes
            ClickableText(
                text = AnnotatedString("Steroid-induced diabetes\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/steroid-induced-diabetes"))
                    context.startActivity(intent)
                }
            )

            // Clickable text for Cystic fibrosis diabetes
            ClickableText(
                text = AnnotatedString("Cystic fibrosis diabetes\n"),
                style = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.primary),
                onClick = {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.diabetes.org.uk/about-diabetes/other-types-of-diabetes/cystic-fibrosis-diabetes"))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun FruitNode(symptom: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(100.dp) // Adjust size as needed
            .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symptom,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SymptomsTreeLayout() {
    val symptoms = listOf(
        "Frequent Urination", "Increased Thirst", "Extreme Hunger",
        "Unexplained Weight Loss", "Fatigue", "Blurred Vision",
        "Slow-healing Sores", "Tingling in hands or feet", "Nausea and Vomiting"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        symptoms.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { symptom ->
                    FruitNode(symptom = symptom)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SymptomsPreview() {
    Symptoms()
}
