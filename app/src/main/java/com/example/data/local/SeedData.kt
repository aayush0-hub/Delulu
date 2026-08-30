package com.example.data.local

import com.example.data.model.ChatMessage
import com.example.data.model.HangoutSignal
import com.example.data.model.IntentMode
import com.example.data.model.Match
import com.example.data.model.Profile
import com.example.data.model.UserProfileSettings

object SeedData {

    fun getDefaultUser(): UserProfileSettings {
        return UserProfileSettings(
            id = 1,
            name = "Alex Rivera",
            age = 25,
            gender = "They/Them",
            bio = "Product designer by day, indie film nerd by night. Always down for specialty pour-overs, night walks, and vinyl hunting.",
            lookingForMode = IntentMode.BOTH,
            radarRadiusMeters = 2000,
            currentVibe = "Looking for coffee & museum buddies ☕🎨",
            isStealthMode = false,
            showExactMeters = true,
            currentLocationName = "Downtown Cultural Hub",
            avatarEmoji = "✨",
            avatarColor = 0xFFFF4081,
            hangoutVibes = listOf("☕ Coffee", "🌿 Walk in the park", "🎲 Game night", "🎵 Live indie music", "🎨 Art gallery crawl"),
            isPhoneVerified = true,
            verifiedPhoneNumber = "+1 (555) 389-9402",
            isPhotoVerified = false,
            photoMatchScore = 0,
            verificationDate = "Aug 2026"
        )
    }

    fun getInitialProfiles(): List<Profile> {
        return listOf(
            Profile(
                id = "p1",
                name = "Maya Lin",
                age = 24,
                gender = "Female",
                occupation = "Architectural Visualizer",
                bio = "Catch me sketching brutalist buildings or exploring hidden plant shops. Big on rooftop chats, matcha, and film photography.",
                mode = IntentMode.DATING,
                relationshipGoal = "Looking for something genuine & spontaneous",
                interests = listOf("Architecture", "Matcha", "35mm Film", "Vinyl Records", "Art Galleries"),
                avatarColor = 0xFFFF2E93,
                avatarEmoji = "🌸",
                distanceMeters = 280,
                angleDegrees = 45f,
                distanceRatio = 0.14f,
                lastActive = "Active now",
                vibeStatus = "At the greenhouse cafe nearby 🌿",
                icebreakerPrompt = "The quickest way to my heart is...",
                icebreakerAnswer = "Knowing the best spot for almond croissants within walking distance.",
                favoriteSpotWithin2km = "Botanical Tea House (350m)",
                hangoutVibes = listOf("☕ Coffee", "🌿 Walk in the park", "🎨 Art gallery crawl", "🧋 Boba & sweet treats"),
                isVerified = true,
                verificationMethod = "Live Selfie Match",
                photoMatchScore = 98
            ),
            Profile(
                id = "p2",
                name = "Julian Thorne",
                age = 26,
                gender = "Male",
                occupation = "Sound Designer & Synthesist",
                bio = "Modular synths, bouldering, and searching for the city's crispest espresso. Looking for weekend climbing partners and music lovers.",
                mode = IntentMode.FRIENDSHIP,
                relationshipGoal = "Workout & Creative project buddies",
                interests = listOf("Bouldering", "Synthesizers", "Espresso", "Sci-Fi Books", "Hiking"),
                avatarColor = 0xFF06B6D4,
                avatarEmoji = "🧗‍♂️",
                distanceMeters = 650,
                angleDegrees = 135f,
                distanceRatio = 0.325f,
                lastActive = "Active 5m ago",
                vibeStatus = "Climbing V5s at Apex Bouldering Gym 🧗",
                icebreakerPrompt = "My ideal Sunday within 2km looks like...",
                icebreakerAnswer = "Two hours at the rock gym followed by cold brew and a bookstore crawl.",
                favoriteSpotWithin2km = "Apex Boulder Club (600m)",
                hangoutVibes = listOf("☕ Coffee", "🧗 Bouldering & climbing", "🎲 Game night", "📚 Book club & reading"),
                isVerified = true,
                verificationMethod = "Selfie + Phone",
                photoMatchScore = 99
            ),
            Profile(
                id = "p3",
                name = "Elena Rostova",
                age = 23,
                gender = "Female",
                occupation = "Pastry Chef & Food Writer",
                bio = "I bake sourdough by dawn and review street food by dusk. Seeking someone to split oversized desserts with.",
                mode = IntentMode.DATING,
                relationshipGoal = "Romantic dinner dates & food adventures",
                interests = listOf("Baking", "Food Tasting", "Jazz Clubs", "Farmers Markets", "Dogs"),
                avatarColor = 0xFFEC4899,
                avatarEmoji = "🥐",
                distanceMeters = 890,
                angleDegrees = 220f,
                distanceRatio = 0.445f,
                lastActive = "Active 12m ago",
                vibeStatus = "Testing pistachio croissants right now 🥐",
                icebreakerPrompt = "A non-negotiable for our first meetup is...",
                icebreakerAnswer = "We must order two different drinks and let each other try.",
                favoriteSpotWithin2km = "Cinnamon & Rye Bakery (850m)",
                hangoutVibes = listOf("☕ Coffee", "🍕 Late night pizza", "🌅 Sunset picnic", "🌮 Street food crawl"),
                isVerified = true,
                verificationMethod = "Phone Confirmed",
                photoMatchScore = 0
            ),
            Profile(
                id = "p4",
                name = "Samir Khan",
                age = 27,
                gender = "Male",
                occupation = "Mobile Software Engineer",
                bio = "Passionate about board game nights, cycling along the canal, and indie coffee roasters. Let's start a trivia team!",
                mode = IntentMode.FRIENDSHIP,
                relationshipGoal = "Board games & cycling group",
                interests = listOf("Board Games", "Cycling", "Indie Rock", "Trivia", "Craft Beer"),
                avatarColor = 0xFF10B981,
                avatarEmoji = "🎲",
                distanceMeters = 1150,
                angleDegrees = 310f,
                distanceRatio = 0.575f,
                lastActive = "Active now",
                vibeStatus = "Looking for 2 more for Catan at The Dice Box 🎲",
                icebreakerPrompt = "If we were in a trivia tournament...",
                icebreakerAnswer = "I've got 90s cinema and geography covered 100%.",
                favoriteSpotWithin2km = "The Dice Box Tabletop Cafe (1.1km)",
                hangoutVibes = listOf("🎲 Game night", "☕ Coffee", "🍹 Craft drinks", "🚲 Casual bike ride"),
                isVerified = true,
                verificationMethod = "Live Selfie Match",
                photoMatchScore = 97
            ),
            Profile(
                id = "p5",
                name = "Chloe Dubois",
                age = 25,
                gender = "Female",
                occupation = "Botanist & Ceramicist",
                bio = "Hands always covered in clay or soil. Big vintage thrifter, plant mom, and lover of indie movie marathons.",
                mode = IntentMode.BOTH,
                relationshipGoal = "Open to dating or deep friendships",
                interests = listOf("Ceramics", "Houseplants", "Thrifting", "Indie Cinema", "Matcha"),
                avatarColor = 0xFF8B5CF6,
                avatarEmoji = "🏺",
                distanceMeters = 420,
                angleDegrees = 80f,
                distanceRatio = 0.21f,
                lastActive = "Active 2m ago",
                vibeStatus = "Throwing pottery in studio #4",
                icebreakerPrompt = "Best local secret within 2km...",
                icebreakerAnswer = "The sunlit rooftop garden above the community ceramic studio.",
                favoriteSpotWithin2km = "Clay & Kiln Studio (400m)",
                hangoutVibes = listOf("🌿 Walk in the park", "🎨 Art gallery crawl", "🪴 Plant shopping", "🍿 Movie & cinema"),
                isVerified = true,
                verificationMethod = "Selfie + Phone",
                photoMatchScore = 96
            ),
            Profile(
                id = "p6",
                name = "Leo Vance",
                age = 28,
                gender = "Male",
                occupation = "Barista & Marathon Runner",
                bio = "Training for the autumn marathon. If you need a running buddy for 5k-10k loops around the park or a coffee connoisseur, I'm your guy.",
                mode = IntentMode.FRIENDSHIP,
                relationshipGoal = "Morning run buddy & workout motivation",
                interests = listOf("Running", "Specialty Coffee", "Podcasts", "Trail Mix", "Dogs"),
                avatarColor = 0xFFF59E0B,
                avatarEmoji = "🏃‍♂️",
                distanceMeters = 1450,
                angleDegrees = 190f,
                distanceRatio = 0.725f,
                lastActive = "Active 18m ago",
                vibeStatus = "Jogging the 2km river loop at 6:30 PM 🏃",
                icebreakerPrompt = "My go-to running tempo is...",
                icebreakerAnswer = "A chill 5:30/km pace with great deep-house playlists.",
                favoriteSpotWithin2km = "Riverside Park Perimeter (1.3km)",
                hangoutVibes = listOf("🌿 Walk in the park", "☕ Coffee", "🐶 Dog park meetup", "🏃 Morning jog & 5k"),
                isVerified = false,
                verificationMethod = "",
                photoMatchScore = 0
            ),
            Profile(
                id = "p7",
                name = "Zara Patel",
                age = 24,
                gender = "Female",
                occupation = "Fashion Stylist & DJ",
                bio = "Electronic music curator, vintage archivist, and sunset chaser. Looking for someone with great taste in tunes and rooftop cocktails.",
                mode = IntentMode.DATING,
                relationshipGoal = "Dating someone who matches my creative energy",
                interests = listOf("House Music", "Vintage Fashion", "Cocktails", "Photography", "Travel"),
                avatarColor = 0xFFFF4081,
                avatarEmoji = "🎧",
                distanceMeters = 730,
                angleDegrees = 275f,
                distanceRatio = 0.365f,
                lastActive = "Active now",
                vibeStatus = "Digging through crates at Beat Records 🎧",
                icebreakerPrompt = "Give me your top song recommendation...",
                icebreakerAnswer = "I'll judge you (nicely) and trade you a hidden gem track.",
                favoriteSpotWithin2km = "Beat Wave Vinyl Shop (700m)",
                hangoutVibes = listOf("🎵 Live indie music", "🍹 Craft drinks", "🌅 Sunset picnic", "☕ Coffee"),
                isVerified = true,
                verificationMethod = "Live Selfie Match",
                photoMatchScore = 99
            ),
            Profile(
                id = "p8",
                name = "Marcus Reed",
                age = 26,
                gender = "Male",
                occupation = "Bookstore Curator & Poet",
                bio = "I run the local used book nook. Lover of rainy afternoons, loose-leaf black tea, quiet corners, and deep conversations.",
                mode = IntentMode.BOTH,
                relationshipGoal = "Deep conversations & shared silence",
                interests = listOf("Literature", "Tea", "Poetry", "Cats", "Acoustic Folk"),
                avatarColor = 0xFF3B82F6,
                avatarEmoji = "📚",
                distanceMeters = 1750,
                angleDegrees = 15f,
                distanceRatio = 0.875f,
                lastActive = "Active 30m ago",
                vibeStatus = "Cataloging new rare poetry volumes 📖",
                icebreakerPrompt = "My favorite book quote right now...",
                icebreakerAnswer = "'We are all stories in the end, make it a good one.'",
                favoriteSpotWithin2km = "Page & Binding Antiquarian Books (1.7km)",
                hangoutVibes = listOf("☕ Coffee", "📚 Book club & reading", "🌿 Walk in the park", "💻 Co-working & study"),
                isVerified = true,
                verificationMethod = "Phone Confirmed",
                photoMatchScore = 0
            )
        )
    }

    fun getInitialHangouts(): List<HangoutSignal> {
        return listOf(
            HangoutSignal(
                id = "h1",
                creatorName = "Maya Lin",
                creatorAvatarEmoji = "🌸",
                creatorColor = 0xFFFF2E93,
                mode = IntentMode.DATING,
                title = "Sunset Iced Matcha & Walk",
                description = "Grabbing iced ceremonial matcha and taking a stroll through the botanical pavilion before sundown. Join me!",
                locationName = "Botanical Tea House",
                distanceMeters = 280,
                timeLabel = "In 20 mins",
                tags = listOf("Coffee/Tea", "Walk", "Sunset"),
                attendeesCount = 1,
                isJoined = false
            ),
            HangoutSignal(
                id = "h2",
                creatorName = "Samir Khan",
                creatorAvatarEmoji = "🎲",
                creatorColor = 0xFF10B981,
                mode = IntentMode.FRIENDSHIP,
                title = "Board Game Table looking for +2 players",
                description = "Playing Wingspan & Catan at The Dice Box. Beginners totally welcome, we have tea and snacks!",
                locationName = "The Dice Box Tabletop Cafe",
                distanceMeters = 1150,
                timeLabel = "Starting at 6:30 PM",
                tags = listOf("Board Games", "Casual", "Group"),
                attendeesCount = 3,
                isJoined = false
            ),
            HangoutSignal(
                id = "h3",
                creatorName = "Julian Thorne",
                creatorAvatarEmoji = "🧗‍♂️",
                creatorColor = 0xFF06B6D4,
                mode = IntentMode.FRIENDSHIP,
                title = "Apex Bouldering Partner Session",
                description = "Projecting some fun V4/V5 problems on the overhang wall. Come climb or give beta!",
                locationName = "Apex Boulder Club",
                distanceMeters = 650,
                timeLabel = "Right now (1hr left)",
                tags = listOf("Climbing", "Fitness", "High Energy"),
                attendeesCount = 2,
                isJoined = false
            ),
            HangoutSignal(
                id = "h4",
                creatorName = "Zara Patel",
                creatorAvatarEmoji = "🎧",
                creatorColor = 0xFFFF4081,
                mode = IntentMode.DATING,
                title = "Vinyl Digging & Espresso Bar",
                description = "Checking out the new shipment of Japanese jazz-funk vinyl at Beat Wave. Who wants to tag along?",
                locationName = "Beat Wave Vinyl Shop",
                distanceMeters = 730,
                timeLabel = "In 45 mins",
                tags = listOf("Music", "Vinyl", "Coffee"),
                attendeesCount = 1,
                isJoined = false
            )
        )
    }

    fun getInitialMatches(): List<Match> {
        val now = System.currentTimeMillis()
        return listOf(
            Match(
                id = "m1",
                profileId = "p1",
                name = "Maya Lin",
                age = 24,
                avatarColor = 0xFFFF2E93,
                avatarEmoji = "🌸",
                mode = IntentMode.DATING,
                distanceMeters = 280,
                matchedAt = now - 3600000 * 2,
                lastMessage = "I'm sitting on the patio table if you want to say hi!",
                lastMessageTimestamp = now - 600000,
                unreadCount = 1,
                isVerified = true,
                verificationMethod = "Live Selfie Match"
            ),
            Match(
                id = "m2",
                profileId = "p2",
                name = "Julian Thorne",
                age = 26,
                avatarColor = 0xFF06B6D4,
                avatarEmoji = "🧗‍♂️",
                mode = IntentMode.FRIENDSHIP,
                distanceMeters = 650,
                matchedAt = now - 86400000,
                lastMessage = "Awesome, let's hit Apex gym tomorrow at 6 PM!",
                lastMessageTimestamp = now - 7200000,
                unreadCount = 0,
                isVerified = true,
                verificationMethod = "Selfie + Phone"
            )
        )
    }

    fun getInitialMessages(): List<ChatMessage> {
        val now = System.currentTimeMillis()
        return listOf(
            ChatMessage(
                matchId = "m1",
                senderIsUser = false,
                text = "Hey Alex! Noticed your 2km radar ping near the art district 😊",
                timestamp = now - 3600000 * 2
            ),
            ChatMessage(
                matchId = "m1",
                senderIsUser = true,
                text = "Hey Maya! Yes, just grabbing some coffee at the roastery nearby. Your architectural sketches look incredible!",
                timestamp = now - 3600000 * 1
            ),
            ChatMessage(
                matchId = "m1",
                senderIsUser = false,
                text = "Thank you so much! I'm sitting on the patio table if you want to say hi!",
                timestamp = now - 600000,
                suggestedLocation = "Botanical Tea House (280m away)"
            ),
            ChatMessage(
                matchId = "m2",
                senderIsUser = true,
                text = "Hey Julian, saw you're on the friendship radar for bouldering partners!",
                timestamp = now - 86400000
            ),
            ChatMessage(
                matchId = "m2",
                senderIsUser = false,
                text = "Yes! Always down for climbing buddies within 2km. What grades do you usually climb?",
                timestamp = now - 80000000
            ),
            ChatMessage(
                matchId = "m2",
                senderIsUser = true,
                text = "Usually V3-V4! Still working on dynamic sloper moves.",
                timestamp = now - 40000000
            ),
            ChatMessage(
                matchId = "m2",
                senderIsUser = false,
                text = "Awesome, let's hit Apex gym tomorrow at 6 PM!",
                timestamp = now - 7200000,
                suggestedLocation = "Apex Boulder Club (650m away)"
            )
        )
    }
}
