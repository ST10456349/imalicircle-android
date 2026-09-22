@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.imalicircle.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.util.UUID

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                iMaliCircleApp()
            }
        }
    }
}

data class Member(
    val name: String,
    val phone: String,
    val role: String = "Member"
)

data class Stokvel(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: Double,
    val frequency: String,
    val members: List<Member> = listOf(),
    val nextPayoutIndex: Int = 0
)

@Composable
fun iMaliCircleApp() {

    var screen by remember { mutableStateOf("login") }
    var stokvels by remember { mutableStateOf(listOf<Stokvel>()) }
    var selectedStokvelId by remember { mutableStateOf<String?>(null) }
    val selectedStokvel = stokvels.find { it.id == selectedStokvelId }

    when (screen) {

        "login" -> {
            LoginScreen(
                onRegister = { screen = "register" },
                onLogin = { screen = "home" }
            )
        }

        "register" -> {
            RegisterScreen(
                onBack = { screen = "login" },
                onRegister = { screen = "home" }
            )
        }

        "home" -> {
            HomeScreen(
                stokvels = stokvels,
                onSettings = { screen = "settings" },
                onCreateGroup = { screen = "createGroup" },
                onLogout = { screen = "login" },
                onStokvelClick = { stokvel ->
                    selectedStokvelId = stokvel.id
                    screen = "detail"
                }
            )
        }

        "settings" -> {
            SettingsScreen(onBack = { screen = "home" })
        }

        "createGroup" -> {
            CreateGroupScreen(
                onCreate = { newStokvel ->
                    stokvels = stokvels + newStokvel
                    screen = "home"
                },
                onCancel = { screen = "home" }
            )
        }

        "detail" -> {
            if (selectedStokvel == null) {
                screen = "home"
            } else {
                StokvelDetailScreen(
                    stokvel = selectedStokvel,
                    onAddMember = { newMember ->
                        stokvels = stokvels.map { existing ->
                            if (existing.id == selectedStokvel.id) {
                                existing.copy(members = existing.members + newMember)
                            } else {
                                existing
                            }
                        }
                    },
                    onAdvancePayout = {
                        stokvels = stokvels.map { existing ->
                            if (existing.id == selectedStokvel.id && existing.members.isNotEmpty()) {
                                existing.copy(
                                    nextPayoutIndex = (existing.nextPayoutIndex + 1) % existing.members.size
                                )
                            } else {
                                existing
                            }
                        }
                    },
                    onBack = { screen = "home" }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "iMali Circle", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Your trusted stokvel management app")
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (phone.isBlank()) {
                    errorMessage = "Please enter your phone number."
                } else if (password.length < 8) {
                    errorMessage = "Password must contain at least 8 characters."
                } else {
                    errorMessage = ""
                    onLogin()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onRegister) {
            Text("Create an account")
        }
    }
}

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegister: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = "Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    name.length < 2 -> errorMessage = "Please enter your full name."
                    phone.length < 9 -> errorMessage = "Please enter a valid phone number."
                    password.length < 8 -> errorMessage = "Password must contain at least 8 characters."
                    password != confirmPassword -> errorMessage = "Passwords do not match."
                    else -> {
                        errorMessage = ""
                        onRegister()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = onBack) {
            Text("Back to Login")
        }
    }
}

@Composable
fun HomeScreen(
    stokvels: List<Stokvel>,
    onSettings: () -> Unit,
    onCreateGroup: () -> Unit,
    onLogout: () -> Unit,
    onStokvelClick: (Stokvel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("iMali Circle") },
                actions = {
                    TextButton(onClick = onSettings) { Text("Settings") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp)
        ) {
            Text(text = "Welcome to iMali Circle!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Manage your stokvel contributions, members and payouts.")
            Spacer(modifier = Modifier.height(25.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "My Stokvels", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (stokvels.isEmpty()) {
                        Text(text = "No stokvels have been created yet.")
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                            items(stokvels) { stokvel ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onStokvelClick(stokvel) }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(text = stokvel.name, style = MaterialTheme.typography.titleMedium)
                                    Text(text = "R${stokvel.amount} - ${stokvel.frequency} - ${stokvel.members.size} member(s)")
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = onCreateGroup, modifier = Modifier.fillMaxWidth()) {
                Text("Create Stokvel")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Log out")
            }
        }
    }
}

@Composable
fun StokvelDetailScreen(
    stokvel: Stokvel,
    onAddMember: (Member) -> Unit,
    onAdvancePayout: () -> Unit,
    onBack: () -> Unit
) {
    var memberName by remember { mutableStateOf("") }
    var memberPhone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val nextUpMember =
        if (stokvel.members.isEmpty()) null
        else stokvel.members[stokvel.nextPayoutIndex % stokvel.members.size]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stokvel.name) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp)
        ) {
            Text(text = "R${stokvel.amount} - ${stokvel.frequency}")
            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Payout rotation", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (nextUpMember == null) "No members yet - add someone below."
                        else "Next up: ${nextUpMember.name}"
                    )
                    if (nextUpMember != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = onAdvancePayout) {
                            Text("Mark payout done / move to next")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Members", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (stokvel.members.isEmpty()) {
                Text(text = "No members added yet.")
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 220.dp)) {
                    items(stokvel.members) { member ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = member.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "${member.role} - ${member.phone}")
                            }
                            if (member == nextUpMember) {
                                Text(text = "Next up", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Add a member", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = memberName,
                onValueChange = { memberName = it },
                label = { Text("Member full name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = memberPhone,
                onValueChange = { memberPhone = it },
                label = { Text("Member phone number") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    when {
                        memberName.length < 2 -> errorMessage = "Please enter the member's full name."
                        memberPhone.length < 9 -> errorMessage = "Please enter a valid phone number."
                        else -> {
                            errorMessage = ""
                            onAddMember(Member(name = memberName, phone = memberPhone))
                            memberName = ""
                            memberPhone = ""
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add member")
            }
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var notifications by remember { mutableStateOf(true) }
    var biometric by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("English") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Language", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            FilterChip(selected = language == "English", onClick = { language = "English" }, label = { Text("English") })
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(selected = language == "isiZulu", onClick = { language = "isiZulu" }, label = { Text("isiZulu") })
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(selected = language == "isiXhosa", onClick = { language = "isiXhosa" }, label = { Text("isiXhosa") })
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Notifications")
            Switch(checked = notifications, onCheckedChange = { notifications = it })
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Biometric login")
            Switch(checked = biometric, onCheckedChange = { biometric = it })
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Save Settings")
        }
    }
}

@Composable
fun CreateGroupScreen(
    onCreate: (Stokvel) -> Unit,
    onCancel: () -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Monthly") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(text = "Create Stokvel", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Stokvel name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Contribution amount (R)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Contribution frequency")

        Row {
            FilterChip(selected = frequency == "Weekly", onClick = { frequency = "Weekly" }, label = { Text("Weekly") })
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(selected = frequency == "Monthly", onClick = { frequency = "Monthly" }, label = { Text("Monthly") })
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val number = amount.toDoubleOrNull()
                when {
                    groupName.length < 2 -> errorMessage = "Please enter a stokvel name."
                    number == null || number <= 0 -> errorMessage = "Please enter a valid contribution amount."
                    else -> {
                        errorMessage = ""
                        onCreate(Stokvel(name = groupName, amount = number, frequency = frequency))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Stokvel")
        }

        TextButton(onClick = onCancel) {
            Text("Cancel")
        }
    }
}