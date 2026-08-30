package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.HangoutSignal
import com.example.data.model.IntentMode
import com.example.data.model.Match
import com.example.data.model.Profile
import com.example.data.model.UserProfileSettings
import com.example.data.repository.NearTwoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val profiles: List<Profile> = emptyList(),
    val filteredProfiles: List<Profile> = emptyList(),
    val matches: List<Match> = emptyList(),
    val hangouts: List<HangoutSignal> = emptyList(),
    val userSettings: UserProfileSettings? = null,
    val selectedModeFilter: IntentMode? = null, // null = All (both dating & friendship)
    val verifiedOnlyFilter: Boolean = false,
    val maxRadiusMeters: Int = 2000,
    val isScanning: Boolean = true,
    val selectedProfile: Profile? = null,
    val activeMatchChat: Match? = null,
    val newMatchCelebration: Match? = null,
    val showCreateHangoutDialog: Boolean = false,
    val showEditProfileDialog: Boolean = false,
    val showLocationPicker: Boolean = false,
    val showVerificationDialog: Boolean = false
)

class MainViewModel : ViewModel() {

    private val repository = NearTwoRepository(AppDatabase.getDatabase())

    private val _selectedModeFilter = MutableStateFlow<IntentMode?>(null)
    private val _verifiedOnlyFilter = MutableStateFlow(false)
    private val _maxRadiusMeters = MutableStateFlow(2000)
    private val _isScanning = MutableStateFlow(true)
    private val _selectedProfile = MutableStateFlow<Profile?>(null)
    private val _activeMatchChat = MutableStateFlow<Match?>(null)
    private val _newMatchCelebration = MutableStateFlow<Match?>(null)
    private val _showCreateHangout = MutableStateFlow(false)
    private val _showEditProfile = MutableStateFlow(false)
    private val _showLocationPicker = MutableStateFlow(false)
    private val _showVerificationDialog = MutableStateFlow(false)

    private val dataFlow = combine(
        repository.allProfiles,
        repository.allMatches,
        repository.allHangouts,
        repository.userSettings
    ) { profiles, matches, hangouts, userSettings ->
        DataState(profiles, matches, hangouts, userSettings)
    }

    private val controlsFlow = combine(
        _selectedModeFilter,
        _verifiedOnlyFilter,
        _maxRadiusMeters,
        _isScanning,
        _selectedProfile
    ) { mode, verifiedOnly, radius, scanning, selected ->
        UiControlsState(mode, verifiedOnly, radius, scanning, selected)
    }

    private val dialogsFlow = combine(
        _activeMatchChat,
        _newMatchCelebration,
        _showCreateHangout,
        _showEditProfile,
        _showLocationPicker
    ) { activeChat, matchCelebration, showHangout, showEdit, showLocation ->
        DialogsState(activeChat, matchCelebration, showHangout, showEdit, showLocation)
    }

    val uiState: StateFlow<UiState> = combine(
        dataFlow,
        controlsFlow,
        dialogsFlow,
        _showVerificationDialog
    ) { data: DataState, controls: UiControlsState, dialogs: DialogsState, showVerification: Boolean ->
        val filteredProfiles = data.profiles.filter { profile ->
            val matchesMode = when (controls.mode) {
                null -> true
                IntentMode.DATING -> profile.mode == IntentMode.DATING || profile.mode == IntentMode.BOTH
                IntentMode.FRIENDSHIP -> profile.mode == IntentMode.FRIENDSHIP || profile.mode == IntentMode.BOTH
                IntentMode.BOTH -> true
            }
            val matchesRadius = profile.distanceMeters <= controls.radius
            val matchesVerified = if (controls.verifiedOnly) profile.isVerified else true
            matchesMode && matchesRadius && matchesVerified
        }

        val filteredHangouts = data.hangouts.filter { hangout ->
            val matchesMode = when (controls.mode) {
                null -> true
                IntentMode.DATING -> hangout.mode == IntentMode.DATING || hangout.mode == IntentMode.BOTH
                IntentMode.FRIENDSHIP -> hangout.mode == IntentMode.FRIENDSHIP || hangout.mode == IntentMode.BOTH
                IntentMode.BOTH -> true
            }
            val matchesRadius = hangout.distanceMeters <= controls.radius
            matchesMode && matchesRadius
        }

        val filteredMatches = data.matches.filter { match ->
            val matchesMode = when (controls.mode) {
                null -> true
                IntentMode.DATING -> match.mode == IntentMode.DATING || match.mode == IntentMode.BOTH
                IntentMode.FRIENDSHIP -> match.mode == IntentMode.FRIENDSHIP || match.mode == IntentMode.BOTH
                IntentMode.BOTH -> true
            }
            val matchesVerified = if (controls.verifiedOnly) match.isVerified else true
            matchesMode && matchesVerified
        }

        UiState(
            profiles = data.profiles,
            filteredProfiles = filteredProfiles,
            matches = filteredMatches,
            hangouts = filteredHangouts,
            userSettings = data.userSettings,
            selectedModeFilter = controls.mode,
            verifiedOnlyFilter = controls.verifiedOnly,
            maxRadiusMeters = controls.radius,
            isScanning = controls.scanning,
            selectedProfile = controls.selected,
            activeMatchChat = dialogs.activeChat,
            newMatchCelebration = dialogs.matchCelebration,
            showCreateHangoutDialog = dialogs.showHangout,
            showEditProfileDialog = dialogs.showEdit,
            showLocationPicker = dialogs.showLocation,
            showVerificationDialog = showVerification
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    private data class DataState(
        val profiles: List<Profile>,
        val matches: List<Match>,
        val hangouts: List<HangoutSignal>,
        val userSettings: UserProfileSettings?
    )

    private data class UiControlsState(
        val mode: IntentMode?,
        val verifiedOnly: Boolean,
        val radius: Int,
        val scanning: Boolean,
        val selected: Profile?
    )

    private data class DialogsState(
        val activeChat: Match?,
        val matchCelebration: Match?,
        val showHangout: Boolean,
        val showEdit: Boolean,
        val showLocation: Boolean
    )

    fun setModeFilter(mode: IntentMode?) {
        _selectedModeFilter.value = mode
    }

    fun setVerifiedOnlyFilter(enabled: Boolean) {
        _verifiedOnlyFilter.value = enabled
    }

    fun toggleVerifiedOnlyFilter() {
        _verifiedOnlyFilter.value = !_verifiedOnlyFilter.value
    }

    fun setMaxRadius(radiusMeters: Int) {
        _maxRadiusMeters.value = radiusMeters
    }

    fun toggleScanning() {
        _isScanning.value = !_isScanning.value
    }

    fun selectProfile(profile: Profile?) {
        _selectedProfile.value = profile
    }

    fun openChat(match: Match?) {
        _activeMatchChat.value = match
        if (match != null) {
            viewModelScope.launch {
                repository.markMatchAsRead(match.id)
            }
        }
    }

    fun dismissMatchCelebration() {
        _newMatchCelebration.value = null
    }

    fun setShowCreateHangout(show: Boolean) {
        _showCreateHangout.value = show
    }

    fun setShowEditProfile(show: Boolean) {
        _showEditProfile.value = show
    }

    fun setShowLocationPicker(show: Boolean) {
        _showLocationPicker.value = show
    }

    fun setShowVerificationDialog(show: Boolean) {
        _showVerificationDialog.value = show
    }

    fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            repository.verifyPhoneNumber(phoneNumber)
        }
    }

    fun verifySelfiePhoto(matchScore: Int = 98) {
        viewModelScope.launch {
            repository.verifySelfiePhoto(matchScore)
        }
    }

    fun resetVerification() {
        viewModelScope.launch {
            repository.resetVerification()
        }
    }

    fun likeProfile(profile: Profile) {
        viewModelScope.launch {
            val createdMatch = repository.likeProfile(profile)
            _selectedProfile.value = null
            if (createdMatch != null) {
                _newMatchCelebration.value = createdMatch
            }
        }
    }

    fun passProfile(profileId: String) {
        viewModelScope.launch {
            repository.passProfile(profileId)
            if (_selectedProfile.value?.id == profileId) {
                _selectedProfile.value = null
            }
        }
    }

    fun resetAllProfiles() {
        viewModelScope.launch {
            repository.resetAllSwipes()
        }
    }

    fun createHangoutSignal(
        title: String,
        description: String,
        locationName: String,
        mode: IntentMode,
        timeLabel: String,
        tags: List<String>
    ) {
        viewModelScope.launch {
            val user = uiState.value.userSettings
            repository.createHangout(
                title = title,
                description = description,
                locationName = locationName,
                mode = mode,
                timeLabel = timeLabel,
                tags = tags,
                creatorName = user?.name ?: "Alex",
                creatorAvatarEmoji = user?.avatarEmoji ?: "✨",
                creatorColor = user?.avatarColor ?: 0xFFFF4081
            )
            _showCreateHangout.value = false
        }
    }

    fun toggleJoinHangout(hangout: HangoutSignal) {
        viewModelScope.launch {
            repository.toggleJoinHangout(hangout)
        }
    }

    fun sendMessage(matchId: String, text: String, location: String? = null) {
        viewModelScope.launch {
            repository.sendMessage(matchId, text, location)
        }
    }

    fun getChatMessages(matchId: String): StateFlow<List<ChatMessage>> {
        return repository.getMessagesForMatch(matchId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun saveProfileSettings(settings: UserProfileSettings) {
        viewModelScope.launch {
            repository.updateUserSettings(settings)
            _showEditProfile.value = false
        }
    }

    fun updateHangoutVibes(vibes: List<String>) {
        viewModelScope.launch {
            val current = uiState.value.userSettings ?: return@launch
            repository.updateUserSettings(current.copy(hangoutVibes = vibes))
        }
    }

    fun relocateTo(neighborhoodName: String) {
        viewModelScope.launch {
            repository.relocateTo(neighborhoodName)
            _showLocationPicker.value = false
        }
    }
}
