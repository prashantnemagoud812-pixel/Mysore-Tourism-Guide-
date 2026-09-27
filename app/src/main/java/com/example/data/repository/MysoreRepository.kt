package com.example.data.repository

import com.example.data.local.BookmarkEntity
import com.example.data.local.ItineraryEntity
import com.example.data.local.MysoreDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class MysoreRepository(private val dao: MysoreDao) {

    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = dao.getAllBookmarks()

    fun isBookmarked(id: String): Flow<Boolean> = dao.isBookmarked(id)

    suspend fun toggleBookmark(attraction: Attraction, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            dao.removeBookmark(attraction.id)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    id = attraction.id,
                    title = attraction.name,
                    category = attraction.category.name,
                    location = attraction.location,
                    rating = attraction.rating,
                    imageUrl = attraction.imageUrl
                )
            )
        }
    }

    suspend fun saveItinerary(
        title: String,
        daysCount: Int,
        interests: List<String>,
        summary: String
    ): Long {
        return dao.insertItinerary(
            ItineraryEntity(
                title = title,
                daysCount = daysCount,
                interestsCsv = interests.joinToString(", "),
                contentSummary = summary
            )
        )
    }

    fun getAllSavedItineraries(): Flow<List<ItineraryEntity>> = dao.getAllItineraries()

    suspend fun deleteItinerary(id: Int) = dao.deleteItinerary(id)

    // Curated high-res, royalty-free photography for Mysore landmarks (using reliable Wikimedia/Unsplash travel imagery)
    fun getAttractions(): List<Attraction> = listOf(
        Attraction(
            id = "mysore_palace",
            name = "Mysore Palace (Amba Vilas)",
            tagLine = "The Jewel of Karnataka & Royal Seat of the Wadiyars",
            category = Category.HERITAGE,
            rating = 4.9f,
            reviewCount = 42800,
            location = "Sayyaji Rao Road, Agrahara",
            distanceKm = 0.8f,
            durationText = "2 – 3 Hours",
            entryFee = "₹100 (Indians) / ₹300 (Foreigners)",
            timings = "10:00 AM – 5:30 PM (Daily)",
            bestTimeToVisit = "Late afternoon followed by night illumination",
            description = "The official residence of the Wadiyar dynasty, Mysore Palace is an extraordinary Indo-Saracenic masterpiece blending Hindu, Mughal, Rajput, and Gothic architectural styles. Adorned with pink marble domes, ornate arches, and an octagonal Kalyana Mantapa with stained glass ceilings from Glasgow, the palace sparkles with nearly 100,000 golden incandescent bulbs on Sunday evenings and during the Dasara festival.",
            historicalHighlights = listOf(
                "Designed by acclaimed British architect Henry Irwin after the 1897 fire",
                "Houses the legendary 200kg solid gold royal throne (Chinnada Simhasana)",
                "Durbar Hall with intricate gold leaf ceiling and peacock-motif mosaic floor",
                "Illuminated with 97,000 bulbs every Sunday from 7:00 PM to 8:00 PM"
            ),
            imageUrl = "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3051,
            longitude = 76.6551,
            audioSnippet = "Welcome to the Amba Vilas Palace. You are standing before the magnificent Durbar Hall where the Maharajas held audience with their royal subjects...",
            isIlluminatedAtNight = true
        ),
        Attraction(
            id = "chamundi_hill",
            name = "Chamundi Hill & Temple",
            tagLine = "Sacred Heights & 1,000 Steps to Divine Panoramas",
            category = Category.HERITAGE,
            rating = 4.8f,
            reviewCount = 28400,
            location = "Chamundi Hill Rd, 13 km from City",
            distanceKm = 11.5f,
            durationText = "2 Hours",
            entryFee = "Free (Special Darshan ₹100)",
            timings = "7:30 AM – 2:00 PM, 3:30 PM – 6:00 PM, 7:30 PM – 9:00 PM",
            bestTimeToVisit = "Early morning for sunrise or dusk for city lights",
            description = "Perched 3,489 feet above sea level, Chamundi Hill offers breathtaking panoramic views of Mysore. The 11th-century temple is dedicated to Goddess Chamundeshwari, the fierce slayer of demon Mahishasura. Pilgrims frequently climb the ancient 1,000 stone stairs carved in 1659, greeting the colossal 16-foot monolithic granite statue of Nandi the Bull midway.",
            historicalHighlights = listOf(
                "Dedicated to the patron deity of Mysore's royal family",
                "Features a 7-tier gopuram towering over 40 meters tall",
                "Halfway up sits the famous monolithic Nandi Bull carved in 1659 AD",
                "Statue of demon Mahishasura wielding a sword and cobra greets visitors at summit"
            ),
            imageUrl = "https://images.unsplash.com/photo-1628080905187-57351658b760?w=900&auto=format&fit=crop&q=80",
            latitude = 12.2741,
            longitude = 76.6713,
            audioSnippet = "As you ascend Chamundi Hill, feel the cool breeze sweeping across the Deccan plateau. The Nandi Bull you pass is carved from a single boulder...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "brindavan_gardens",
            name = "Brindavan Gardens & KRS Dam",
            tagLine = "Terraced Mughal Gardens & Dancing Musical Fountains",
            category = Category.NATURE,
            rating = 4.6f,
            reviewCount = 31200,
            location = "KRS Dam Road, Mandya District",
            distanceKm = 19.2f,
            durationText = "3 Hours",
            entryFee = "₹50 (Adults) / ₹10 (Children)",
            timings = "6:30 AM – 9:00 PM (Fountain Show: 6:30 PM – 7:30 PM)",
            bestTimeToVisit = "Late afternoon to catch the sunset and evening light & sound fountain show",
            description = "Constructed in 1932 beneath the colossal Krishnaraja Sagara (KRS) Dam across the Cauvery River, Brindavan Gardens spans 60 lush acres designed on the symmetry of Shalimar Gardens. The highlight is the evening musical fountain, where vibrant jets of water dance synchronously to patriotic and classical tunes amidst illuminated flower beds.",
            historicalHighlights = listOf(
                "Conceptualized by Sir Mirza Ismail and engineered by Sir M. Visvesvaraya",
                "Over 60 acres of symmetrical terraces, gazebos, and rose beds",
                "Famous backdrop for hundreds of Indian cinema musical sequences",
                "Features boating across the garden canal beneath the illuminated dam"
            ),
            imageUrl = "https://images.unsplash.com/photo-1596401057633-54a8fe8ef647?w=900&auto=format&fit=crop&q=80",
            latitude = 12.4243,
            longitude = 76.5746,
            audioSnippet = "The rhythmic fountains of Brindavan burst into a symphony of lights as twilight descends over the majestic waters of the Cauvery...",
            isIlluminatedAtNight = true
        ),
        Attraction(
            id = "philomenas_cathedral",
            name = "St. Philomena’s Cathedral",
            tagLine = "Twin Neo-Gothic Spires Soaring 175 Feet",
            category = Category.HERITAGE,
            rating = 4.7f,
            reviewCount = 19600,
            location = "Ashoka Road, Lashkar Mohalla",
            distanceKm = 2.1f,
            durationText = "45 Mins – 1 Hour",
            entryFee = "Free",
            timings = "5:00 AM – 6:00 PM",
            bestTimeToVisit = "Morning sunlight when stained glass illuminates the nave",
            description = "One of the tallest and most magnificent churches in Asia, St. Philomena's was designed in 1936 by French architect Daly, inspired by the Gothic majesty of Cologne Cathedral. Its twin spires soar 175 feet into the sky, housing vibrant stained glass windows imported from France and peaceful subterranean catacombs containing relics of Saint Philomena.",
            historicalHighlights = listOf(
                "Foundation laid in 1933 by Maharaja Krishnaraja Wadiyar IV",
                "Built in the shape of a Latin cross with German Gothic spires",
                "Exquisite stained glass windows depict the Nativity, Last Supper, and Crucifixion",
                "Catacomb beneath the altar preserves a relic of St. Philomena from the 3rd century"
            ),
            imageUrl = "https://images.unsplash.com/photo-1548013146-72479768bada?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3211,
            longitude = 76.6575,
            audioSnippet = "Step inside the sanctuary. Notice how the colored light filters through the 90-year-old French stained glass, illuminating the Gothic arches...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "devaraja_market",
            name = "Devaraja Market",
            tagLine = "135-Year-Old Sensory Spectacle of Spices & Silk",
            category = Category.SHOPPING,
            rating = 4.7f,
            reviewCount = 22100,
            location = "Sayyaji Rao Road, Shivarampet",
            distanceKm = 1.0f,
            durationText = "1.5 – 2 Hours",
            entryFee = "Free",
            timings = "6:30 AM – 8:30 PM (Daily)",
            bestTimeToVisit = "Early morning for fresh flower arrivals or late afternoon",
            description = "Dating back to the reign of Chamaraja Wadiyar IX in the late 19th century, Devaraja Market is a quintessential Mysore experience. Wander through covered stone corridors stacked with vibrant conical pyramids of colored kumkum, fragrant mounds of Mysore mallige (jasmine), fresh Nanjangud bananas, cold-pressed sandalwood oils, and pure betel leaves.",
            historicalHighlights = listOf(
                "More than 800 heritage vendor stalls operating continuously since 1886",
                "Renowned center for traditional Mysore Jasmine (Mallige) flower auctions",
                "Authentic source for natural attar, pure sandalwood incense, and cane jaggery",
                "Classic wooden colonnades and bustling agrahara market walkways"
            ),
            imageUrl = "https://images.unsplash.com/photo-1596178065887-1198b6148b2b?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3113,
            longitude = 76.6521,
            audioSnippet = "Breathe in the rich scents of jasmine, cardamom, and Mysore sandalwood. Here in Devaraja Market, trade has flowed unchanged for over a century...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "karanji_lake",
            name = "Karanji Lake & Nature Park",
            tagLine = "India's Largest Walk-Through Aviary & Butterfly Sanctuary",
            category = Category.NATURE,
            rating = 4.6f,
            reviewCount = 14300,
            location = "Siddhartha Layout, Near Zoo",
            distanceKm = 2.8f,
            durationText = "2 Hours",
            entryFee = "₹10 (Adults) / ₹5 (Children)",
            timings = "8:30 AM – 5:30 PM (Closed Tuesdays)",
            bestTimeToVisit = "Morning for bird-watching and peaceful boating",
            description = "Nestled at the base of Chamundi Hill, Karanji Lake spans 90 pristine hectares maintained by Mysore Zoo. It boasts India's largest walk-through aviary, towering 20 meters high, where visitors walk amidst peacocks, painted storks, and herons. The lake also houses an enchanting butterfly park, watchtower, and eco-friendly paddle boating.",
            historicalHighlights = listOf(
                "Originally constructed by the King of Mysore as a percolation reservoir",
                "Home to over 147 recorded species of migratory and resident birds",
                "Walk-through aviary spans 60m x 40m with a cascading waterfall inside",
                "Houses the Regional Museum of Natural History along its southern banks"
            ),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3025,
            longitude = 76.6710,
            audioSnippet = "Listen to the birdsong echoing across Karanji Lake. This aviary lets you walk freely amongst rare water birds and peafowl in their natural habitat...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "mysore_zoo",
            name = "Sri Chamarajendra Zoological Gardens",
            tagLine = "World-Class Heritage Zoo Founded in 1892",
            category = Category.NATURE,
            rating = 4.8f,
            reviewCount = 48500,
            location = "Indiranagar, Ittige Gudu",
            distanceKm = 2.2f,
            durationText = "3 Hours",
            entryFee = "₹100 (Weekdays) / ₹120 (Weekends)",
            timings = "8:30 AM – 5:30 PM (Closed Tuesdays)",
            bestTimeToVisit = "Morning hours when animals are most active",
            description = "Covering 157 acres, Mysore Zoo is one of the oldest and most celebrated zoos in the world. Established by Maharaja Chamarajendra Wadiyar in 1892, it is famous for spacious moated habitats, botanical gardens with centuries-old trees, and its role as a global breeding center for endangered species including white tigers, Asiatic lions, and giraffes.",
            historicalHighlights = listOf(
                "Founded as a private royal menagerie in 1892 by the Maharaja",
                "First zoo in the country to adopt international barless open enclosures",
                "Pioneering wildlife adoption program supported by global animal lovers",
                "Features giraffes, African elephants, tapirs, anacondas, and gorillas"
            ),
            imageUrl = "https://images.unsplash.com/photo-1534188753412-3e26d0d618d6?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3019,
            longitude = 76.6644,
            audioSnippet = "Under the royal patronage of the Mysore Maharajas, this sanctuary pioneered ethical open-air habitats for wildlife across Asia...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "srirangapatna",
            name = "Srirangapatna Fort & Palace",
            tagLine = "Historic River Island of Tipu Sultan ‘The Tiger of Mysore’",
            category = Category.HERITAGE,
            rating = 4.7f,
            reviewCount = 26500,
            location = "Srirangapatna Island, 16 km North",
            distanceKm = 16.0f,
            durationText = "3 – 4 Hours",
            entryFee = "₹25 (Monuments) / ₹300 (Foreigners)",
            timings = "9:00 AM – 6:00 PM (Daily)",
            bestTimeToVisit = "Morning to afternoon; pair with Ranganathittu Bird Sanctuary",
            description = "Encircled by the bifurcating Cauvery River, Srirangapatna was the formidable capital of Mysore under Hyder Ali and Tipu Sultan. Explore Dariya Daulat Bagh (Tipu's summer palace crafted entirely of teakwood with murals), the Gumbaz mausoleum, the dungeon where British officers were imprisoned, and the historic 9th-century Ranganathaswamy Temple.",
            historicalHighlights = listOf(
                "Epicenter of the four Anglo-Mysore Wars ending in 1799",
                "Dariya Daulat Bagh features intricate murals depicting the Battle of Pollilur",
                "Houses the subterranean Bailey's Dungeon and Tipu's death place monument",
                "Sacred Sangama confluence of three holy river channels nearby"
            ),
            imageUrl = "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=900&auto=format&fit=crop&q=80",
            latitude = 12.4181,
            longitude = 76.6946,
            audioSnippet = "You are on the island fortress of Srirangapatna. Within these teak walls, Tipu Sultan commanded the legendary rocket artillery brigades of Mysore...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "jaganmohan_palace",
            name = "Jaganmohan Palace & Art Gallery",
            tagLine = "Royal Art Pavilion with Original Raja Ravi Varma Masterpieces",
            category = Category.CULTURE,
            rating = 4.6f,
            reviewCount = 11200,
            location = "Subbarayanakere, Chamrajpura",
            distanceKm = 0.9f,
            durationText = "1.5 Hours",
            entryFee = "₹75 (Adults) / ₹30 (Children)",
            timings = "10:00 AM – 5:30 PM (Daily)",
            bestTimeToVisit = "Afternoon during warm hours to enjoy air-conditioned royal gallery",
            description = "Built in 1861 as an alternate royal palace for the Wadiyar kings, this three-storied palace was converted into the Sri Jayachamarajendra Art Gallery in 1915. It houses one of the largest collections of original paintings by celebrated master Raja Ravi Varma, exquisite ivory carvings, antique clocks, and royal musical instruments.",
            historicalHighlights = listOf(
                "Showcases Ravi Varma’s famous painting 'Lady with the Lamp' (Glow of Hope)",
                "Early Legislative Council meetings of the Mysore Princely State were held here",
                "Houses rare sandalwood carvings, gold leaf Tanjore paintings, and antique weapons",
                "Unique mechanical clock where miniature toy soldiers march every hour"
            ),
            imageUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=900&auto=format&fit=crop&q=80",
            latitude = 12.3079,
            longitude = 76.6499,
            audioSnippet = "Look closely at the painting 'Glow of Hope' by S.L. Haldankar. Notice how the single candle flame appears to illuminate the translucent fabric...",
            isIlluminatedAtNight = false
        ),
        Attraction(
            id = "lalitha_mahal",
            name = "Lalitha Mahal Palace",
            tagLine = "Pure White Italian Marble Palace on the Chamundi Foothills",
            category = Category.STAY,
            rating = 4.7f,
            reviewCount = 8900,
            location = "Lalitha Mahal Nagar, Siddhartha Layout",
            distanceKm = 5.2f,
            durationText = "1 – 2 Hours",
            entryFee = "₹100 (Visitors) or Royal High Tea",
            timings = "10:00 AM – 8:00 PM",
            bestTimeToVisit = "Afternoon for Royal High Tea or evening for cocktails",
            description = "Built in 1921 by Maharaja Krishnaraja Wadiyar IV to host the Viceroy of India, Lalitha Mahal is an immaculate white Italian Renaissance chateau. Set in terraced gardens with twin ionic columns, a central dome styled after St. Paul's Cathedral in London, Belgian glass chandeliers, and a grand Italian marble staircase, it now operates as an elite heritage luxury stay.",
            historicalHighlights = listOf(
                "Modeled after St. Paul's Cathedral in London by architect E.W. Fritchley",
                "Features grand ballroom with spring wooden dance floor and cut-glass chandeliers",
                "Original Otis birdcage elevator operated by royal liveried attendants",
                "Served as the filming venue for numerous classic Indian period dramas"
            ),
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=900&auto=format&fit=crop&q=80",
            latitude = 12.2987,
            longitude = 76.6872,
            audioSnippet = "As you step into the grand dining salon beneath the dome, imagine the royal banquets hosted here for the visiting British royalty...",
            isIlluminatedAtNight = true
        )
    )

    fun getFoodItems(): List<FoodItem> = listOf(
        FoodItem(
            id = "mysore_masala_dosa",
            name = "Mysore Masala Dosa",
            kannadaName = "ಮೈಸೂರು ಮಸಾಲ ದೋಸೆ",
            shortDescription = "Crispy golden crepe layered with fiery red garlic-chili paste and dollops of white butter.",
            fullStory = "Unlike ordinary dosas, the authentic Mysore Masala Dosa has a crisp exterior with a spongy, soft inner lining. It is smeared with a secret red chutney made from Byadagi chilies, garlic, and fried gram, then filled with gently spiced potato palya and crowned with a dollop of fresh white unsalted butter (benne).",
            priceRange = "₹60 – ₹120",
            famousPlaces = listOf("Original Vinayaka Mylari (Agrahara)", "Gayatri Tiffin Room (GTR)", "Dasaprakash", "Hotel Hanumanthu"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800&auto=format&fit=crop&q=80",
            tag = "Must-Eat Iconic"
        ),
        FoodItem(
            id = "mysore_pak",
            name = "Royal Mysore Pak",
            kannadaName = "ಮೈಸೂರು ಪಾಕ್",
            shortDescription = "Melt-in-your-mouth royal dessert made with pure desi ghee, gram flour, and cardamoms.",
            fullStory = "Invented in 1935 right in the royal kitchen of Mysore Palace by master chef Kakasura Madappa. When Maharaja Krishnaraja Wadiyar tasted this warm sweet made of besan, ghee, and sugar syrup, he was enchanted and named it 'Mysore Paaka' (paaka meaning sugar syrup concoction). Madappa's direct descendants still run the legendary Guru Sweet Mart today.",
            priceRange = "₹120 – ₹350 / box",
            famousPlaces = listOf("Guru Sweet Mart (Sayyaji Rao Rd)", "Mahalakshmi Sweets", "Sri Krishna Sweets", "Anand Sweets"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=800&auto=format&fit=crop&q=80",
            tag = "Royal Heritage"
        ),
        FoodItem(
            id = "mysore_filter_coffee",
            name = "South Indian Filter Coffee",
            kannadaName = "ಫಿಲ್ಟರ್ ಕಾಫಿ",
            shortDescription = "Aromatic chicory blend freshly brewed in brass filters, frothed with steaming whole milk.",
            fullStory = "Sourced from the misty coffee estates of nearby Coorg and Chikmagalur, Mysore filter coffee is an art form. Poured back and forth between a brass dabarah and tumbler to create a dense, golden froth called 'kaapi nurai'. The aroma alone awakens the city at 6:00 AM.",
            priceRange = "₹25 – ₹50",
            famousPlaces = listOf("Mylari Agrahara", "Brahmins Cafe", "Malgudi Cafe", "Gayatri Tiffin Room"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800&auto=format&fit=crop&q=80",
            tag = "Morning Ritual"
        ),
        FoodItem(
            id = "maddur_vada",
            name = "Maddur Vada",
            kannadaName = "ಮದ್ದೂರು ವಡೆ",
            shortDescription = "Crispy savory snack made with rice flour, semolina, roasted onions, curry leaves, and green chilies.",
            fullStory = "Originating from Maddur town just 45 minutes from Mysore along the railway line, this crunchy fritter was invented in 1917 at Maddur railway station. Crisp on the outside and soft within, it is seasoned with hing (asafoetida), curry leaves, and grated ginger.",
            priceRange = "₹20 – ₹45",
            famousPlaces = listOf("Maddur Tiffany's (Expressway)", "Guru Prasad", "Sri Raghavendra Bhavan"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&auto=format&fit=crop&q=80",
            tag = "Crispy Snack"
        ),
        FoodItem(
            id = "mysore_churumuri",
            name = "Mysore Churumuri",
            kannadaName = "ಚುರುಮುರಿ",
            shortDescription = "Spicy street bhel tossed with puffed rice, grated raw mango, carrots, and cold-pressed coconut oil.",
            fullStory = "The quintessential evening snack eaten around Kukkarahalli Lake and Chamundi foothill carts. Puffed rice is tossed dynamically in a stainless steel bowl with fresh raw mango, shredded carrot, roasted peanuts, chili-garlic paste, and a splash of raw coconut oil and lime.",
            priceRange = "₹30 – ₹50",
            famousPlaces = listOf("Kukkarahalli Lake entrance cart", "Devaraja Market carts", "D. Devaraj Urs Road"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800&auto=format&fit=crop&q=80",
            tag = "Street Food"
        ),
        FoodItem(
            id = "mallige_idli",
            name = "Mallige Idli & Sambar",
            kannadaName = "ಮಲ್ಲಿಗೆ ಇಡ್ಲಿ",
            shortDescription = "Feather-light steamed rice cakes as soft as jasmine petals, served with piping hot dal sambar.",
            fullStory = "Named after Mysore's renowned Mallige (jasmine) flower because of its pristine white color and ultra-soft, airy texture. Made using a special ferment of local rice and beaten rice (poha), paired with roasted chana coconut chutney.",
            priceRange = "₹40 – ₹80",
            famousPlaces = listOf("Hotel Original Mylari", "Meghana Foods", "Sri Durga Bhavan"),
            isVegetarian = true,
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&auto=format&fit=crop&q=80",
            tag = "Breakfast Classic"
        )
    )

    fun getExperiences(): List<ExperienceItem> = listOf(
        ExperienceItem(
            id = "exp_palace_night",
            title = "Palace Illumination Walk",
            duration = "1.5 Hours",
            category = "Royal Night",
            priceGuide = "₹100 – ₹150",
            description = "Witness the breathtaking sight of 100,000 bulbs illuminating Mysore Palace simultaneously against the dark sky, accompanied by the live royal police brass band.",
            bestTime = "Sunday & Public Holidays 6:45 PM",
            location = "Mysore Palace Courtyard",
            highlights = listOf("Live Karnataka Police Royal Band", "Golden illumination countdown", "Unparalleled photography angles"),
            imageUrl = "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800&auto=format&fit=crop&q=80"
        ),
        ExperienceItem(
            id = "exp_chamundi_sunrise",
            title = "Chamundi 1,000-Step Dawn Trek",
            duration = "2.5 Hours",
            category = "Heritage & Fitness",
            priceGuide = "Free",
            description = "Climb the historic stone staircase carved in 1659 AD through tranquil forest, meet the holy monolithic Nandi Bull, and watch the sun rise over the City of Palaces.",
            bestTime = "5:30 AM – 7:30 AM",
            location = "Chamundi Hill Footpath Base",
            highlights = listOf("Ancient 1,000 stone steps", "16-foot monolithic granite Nandi", "Panoramic dawn city views"),
            imageUrl = "https://images.unsplash.com/photo-1628080905187-57351658b760?w=800&auto=format&fit=crop&q=80"
        ),
        ExperienceItem(
            id = "exp_food_trail",
            title = "Old Mysore Food & Heritage Safari",
            duration = "3 Hours",
            category = "Gastronomy",
            priceGuide = "₹400 – ₹800",
            description = "Taste your way through century-old cafes: original Mylari benne dosa, royal Mysore Pak at Guru Sweets, hot filter coffee, and spiced churumuri.",
            bestTime = "8:30 AM or 4:30 PM",
            location = "Agrahara & Sayyaji Rao Road",
            highlights = listOf("Original 1938 Mylari dosa recipe", "Direct descendants of Mysore Pak inventor", "Freshly grounded filter kaapi"),
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800&auto=format&fit=crop&q=80"
        ),
        ExperienceItem(
            id = "exp_silk_sandalwood",
            title = "Mysore Silk & Sandalwood Workshop",
            duration = "2 Hours",
            category = "Artisan Craft",
            priceGuide = "Free Entry",
            description = "Visit KSIC Mysore Silk weaving factory to witness real gold Zari spun into lustrous GI-tagged Mysore silk sarees, followed by the Government Sandalwood Oil Factory.",
            bestTime = "10:30 AM – 3:30 PM",
            location = "Mananthavady Road",
            highlights = listOf("100% pure gold Zari thread demonstration", "Government GI certification", "Rare pure sandalwood essential oil"),
            imageUrl = "https://images.unsplash.com/photo-1596178065887-1198b6148b2b?w=800&auto=format&fit=crop&q=80"
        ),
        ExperienceItem(
            id = "exp_ashtanga_yoga",
            title = "Authentic Ashtanga Yoga in Gokulam",
            duration = "1.5 Hours",
            category = "Wellness",
            priceGuide = "₹500 – ₹1,000",
            description = "Mysore is recognized as the world birthplace of Ashtanga Vinyasa Yoga (founded by Pattabhi Jois). Join a morning practice in leafy, bohemian Gokulam.",
            bestTime = "6:30 AM – 8:00 AM",
            location = "Gokulam 3rd Stage",
            highlights = listOf("Global yoga hub", "Traditional Mysore-style self-practice", "Pranayama and meditation"),
            imageUrl = "https://images.unsplash.com/photo-1545205597-3d9d02c29597?w=800&auto=format&fit=crop&q=80"
        ),
        ExperienceItem(
            id = "exp_ranganathittu_boat",
            title = "Ranganathittu River Safari",
            duration = "3 Hours",
            category = "Wildlife",
            priceGuide = "₹150 – ₹400",
            description = "Drift along the serene waters of the Cauvery River on a wooden boat amidst nesting pelicans, storks, spoonbills, and marsh crocodiles basking on river islets.",
            bestTime = "8:00 AM – 11:00 AM",
            location = "Ranganathittu (3 km from Srirangapatna)",
            highlights = listOf("Bird sanctuary boat safari", "Marsh crocodiles in wild", "Over 200 bird species"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80"
        )
    )

    fun getTravelInfo(): TravelInfo = TravelInfo(
        bestSeason = "Winter & Festive Season (October – March)",
        bestMonths = "Dasara festival (Sept/Oct) and cool winter months (Nov – Feb)",
        airport = "Mysore Airport (MYQ) at Mandakalli (12 km) or Kempegowda Bengaluru International (BLR - 165 km / 2 hrs via expressway)",
        railHub = "Mysore Junction (MYS) with frequent Vande Bharat Express connectivity from Bengaluru and Chennai",
        roadExpressway = "10-lane Bengaluru-Mysuru Expressway (NH-275) with travel time under 90 minutes",
        gettingAround = listOf(
            TransportOption("Auto Rickshaw", "Ubiquitous, affordable; ask for meter or verify fare beforehand (minimum ₹30).", "₹30 – ₹150 in city", "Great for narrow Agrahara lanes and market visits."),
            TransportOption("Heritage Tonga", "Traditional horse carriages around the Palace perimeter.", "₹200 – ₹400 / ride", "A royal novelty experience around Chamaraja Circle."),
            TransportOption("App Cabs (Ola/Uber)", "Plentiful for Chamundi Hill, KRS Dam, and Srirangapatna.", "₹150 – ₹600", "Convenient for day-long trips and outstation travel."),
            TransportOption("KSRTC Heritage Buses", "Open-top double decker 'Ambaari' tourist buses operate during festivals.", "₹250 / pass", "Panoramic open-roof view of illuminated monuments.")
        ),
        tips = listOf(
            "Sundays are the best days to see the Mysore Palace fully illuminated with 100,000 bulbs (7:00 PM – 8:00 PM).",
            "Footwear must be deposited before entering the Palace interior halls; socks are recommended.",
            "Purchase Mysore Silk directly from KSIC outlets to ensure genuine gold zari with GI tag certification.",
            "Start early (by 6:30 AM) if climbing Chamundi Hill to avoid midday heat.",
            "Try Original Vinayaka Mylari by 8:30 AM as they often run out of their signature butter dosa by 11:00 AM!"
        ),
        dayTrips = listOf(
            DayTrip("Coorg (Madikeri)", 118, "2.5 Hours", "Misty coffee plantations, Abbey Falls, Raja's Seat, Tibetan Golden Temple", "https://images.unsplash.com/photo-1596401057633-54a8fe8ef647?w=600"),
            DayTrip("Bandipur National Park", 78, "1.5 Hours", "Tiger reserve, wild elephant safaris, Kabini backwaters", "https://images.unsplash.com/photo-1534188753412-3e26d0d618d6?w=600"),
            DayTrip("Somnathpur (Chennakeshava)", 35, "50 Mins", "13th-century Hoysala stone carving masterpiece and UNESCO World Heritage", "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=600"),
            DayTrip("Shivasamudram Falls", 75, "1.5 Hours", "Twin roaring Cauvery waterfalls (Gaganachukki & Bharachukki)", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600")
        )
    )

    fun getMapMarkers(): List<MysoreMapMarker> = listOf(
        MysoreMapMarker("mysore_palace", "Mysore Palace", "Royal Seat & Grand Durbar", Category.HERITAGE, 12.3051, 76.6551, 4.9f, "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=400"),
        MysoreMapMarker("chamundi_hill", "Chamundi Hill", "Temple & 1000 Steps", Category.HERITAGE, 12.2741, 76.6713, 4.8f, "https://images.unsplash.com/photo-1628080905187-57351658b760?w=400"),
        MysoreMapMarker("brindavan_gardens", "Brindavan Gardens", "Terraced Gardens & KRS Dam", Category.NATURE, 12.4243, 76.5746, 4.6f, "https://images.unsplash.com/photo-1596401057633-54a8fe8ef647?w=400"),
        MysoreMapMarker("devaraja_market", "Devaraja Market", "135-Year Spices & Jasmine", Category.SHOPPING, 12.3113, 76.6521, 4.7f, "https://images.unsplash.com/photo-1596178065887-1198b6148b2b?w=400"),
        MysoreMapMarker("philomenas_cathedral", "St. Philomena's", "Neo-Gothic 175ft Spires", Category.HERITAGE, 12.3211, 76.6575, 4.7f, "https://images.unsplash.com/photo-1548013146-72479768bada?w=400"),
        MysoreMapMarker("mysore_zoo", "Mysore Zoo", "Heritage Zoological Gardens", Category.NATURE, 12.3019, 76.6644, 4.8f, "https://images.unsplash.com/photo-1534188753412-3e26d0d618d6?w=400"),
        MysoreMapMarker("karanji_lake", "Karanji Lake", "Walk-through Aviary & Boating", Category.NATURE, 12.3025, 76.6710, 4.6f, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400"),
        MysoreMapMarker("srirangapatna", "Srirangapatna", "Tipu Sultan's Fort Island", Category.HERITAGE, 12.4181, 76.6946, 4.7f, "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=400"),
        MysoreMapMarker("lalitha_mahal", "Lalitha Mahal Palace", "White Marble Heritage Hotel", Category.STAY, 12.2987, 76.6872, 4.7f, "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400"),
        MysoreMapMarker("mylari_food", "Original Vinayaka Mylari", "Legendary Butter Dosa", Category.FOOD, 12.3045, 76.6578, 4.9f, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=400"),
        MysoreMapMarker("guru_sweets", "Guru Sweet Mart", "Birthplace of Mysore Pak", Category.FOOD, 12.3090, 76.6520, 4.8f, "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=400")
    )

    fun generateItinerary(days: Int, selectedInterests: Set<String>): List<GeneratedDayPlan> {
        val hasFood = selectedInterests.contains("Food") || selectedInterests.isEmpty()
        val hasNature = selectedInterests.contains("Nature") || selectedInterests.isEmpty()
        val hasHeritage = selectedInterests.contains("Heritage") || selectedInterests.isEmpty()
        val hasShopping = selectedInterests.contains("Shopping") || selectedInterests.isEmpty()
        val hasPhoto = selectedInterests.contains("Photography") || selectedInterests.isEmpty()

        val plans = mutableListOf<GeneratedDayPlan>()

        // DAY 1: Royal Heart of Mysore
        plans.add(
            GeneratedDayPlan(
                dayNumber = 1,
                themeTitle = "The Royal Heart & Golden Splendor",
                slots = listOf(
                    ItinerarySlot(
                        timeSlot = "Morning (8:30 AM)",
                        activityTitle = "Heritage Breakfast & Durbar Grandeur",
                        placeName = if (hasFood) "Original Vinayaka Mylari & Mysore Palace" else "Mysore Palace (Amba Vilas)",
                        description = "Start with iconic melt-in-mouth butter dosa, then explore the magnificent Durbar Hall and golden royal throne.",
                        duration = "3.5 Hours",
                        travelTip = "Arrive at the palace by 10 AM to beat tour bus queues; deposit footwear at North Gate.",
                        category = Category.HERITAGE
                    ),
                    ItinerarySlot(
                        timeSlot = "Afternoon (1:30 PM)",
                        activityTitle = if (hasShopping) "Scent & Silk Bazaar Walk" else "Jaganmohan Art Gallery",
                        placeName = if (hasShopping) "Devaraja Market & KSIC Silk" else "Jaganmohan Palace Gallery",
                        description = if (hasShopping) "Wander through centuries-old spice and jasmine aisles and pick up certified sandalwood oils." else "Admire Raja Ravi Varma's legendary 'Glow of Hope' and royal antiques.",
                        duration = "2 Hours",
                        travelTip = "Devaraja market is shaded and bustling with vibrant photo opportunities.",
                        category = if (hasShopping) Category.SHOPPING else Category.CULTURE
                    ),
                    ItinerarySlot(
                        timeSlot = "Evening (4:30 PM)",
                        activityTitle = "Sunset Pilgrimage & Sacred Panorama",
                        placeName = "Chamundi Hill & Monolithic Nandi",
                        description = "Ascend the hill to seek blessings at the 11th-century Chamundeshwari Temple and watch the sunset illuminate the city.",
                        duration = "2.5 Hours",
                        travelTip = "Pause halfway down to photograph the 16-foot monolithic Nandi Bull carved from black granite.",
                        category = Category.HERITAGE
                    ),
                    ItinerarySlot(
                        timeSlot = "Night (7:30 PM)",
                        activityTitle = "Palace Illumination & Traditional Thali",
                        placeName = "Mysore Palace Gates & Dasaprakash",
                        description = "Gaze at the palace lit up with 100,000 bulbs (on Sundays/Dasara) and relish a royal South Indian plantain leaf feast.",
                        duration = "2 Hours",
                        travelTip = "End your night with fresh melt-in-the-mouth Mysore Pak from Guru Sweet Mart.",
                        category = Category.FOOD
                    )
                )
            )
        )

        if (days >= 2) {
            // DAY 2: Waterfalls, Fountains & Flora
            plans.add(
                GeneratedDayPlan(
                    dayNumber = 2,
                    themeTitle = "Gardens, Wild Preserves & Dancing Waters",
                    slots = listOf(
                        ItinerarySlot(
                            timeSlot = "Morning (8:30 AM)",
                            activityTitle = if (hasNature) "Heritage Zoo & Aviary Stroll" else "St. Philomena's Gothic Cathedral",
                            placeName = if (hasNature) "Sri Chamarajendra Zoo & Karanji Lake" else "St. Philomena's Cathedral",
                            description = "Breathe fresh morning air while walking through one of Asia's finest zoos and India's largest walk-through aviary.",
                            duration = "3 Hours",
                            travelTip = "Take an electric battery cart inside the zoo if travelling with elders or children.",
                            category = Category.NATURE
                        ),
                        ItinerarySlot(
                            timeSlot = "Afternoon (1:00 PM)",
                            activityTitle = "Royal High Tea & Italian Renaissance",
                            placeName = "Lalitha Mahal Palace",
                            description = "Savor lunch or colonial afternoon tea under the Belgian glass domes of this pure white Italian marble palace.",
                            duration = "2 Hours",
                            travelTip = "Dress smart-casual; the ballroom terrace offers stunning vistas towards Chamundi Hill.",
                            category = Category.STAY
                        ),
                        ItinerarySlot(
                            timeSlot = "Late Afternoon (4:30 PM)",
                            activityTitle = "Mughal Terraces & Cauvery Reservoir",
                            placeName = "Brindavan Gardens (KRS Dam)",
                            description = "Walk along symmetrical fountains, fragrant rose gazebos, and take a boat across the reservoir.",
                            duration = "2.5 Hours",
                            travelTip = "The musical fountain light show starts at 6:30 PM. Arrive early to secure bench seating.",
                            category = Category.NATURE
                        ),
                        ItinerarySlot(
                            timeSlot = "Night (8:00 PM)",
                            activityTitle = "Riverside Dinner & Maddur Vada",
                            placeName = "KRS Dam Road Eateries",
                            description = "Enjoy crisp freshly fried Maddur Vada with coconut chutney and strong chicory filter coffee on the return drive.",
                            duration = "1 Hour",
                            travelTip = "Autos and cabs are easily pre-booked for the return trip to Mysore city.",
                            category = Category.FOOD
                        )
                    )
                )
            )
        }

        if (days >= 3) {
            // DAY 3: Fortress Island of Tipu Sultan & Sanctuary
            plans.add(
                GeneratedDayPlan(
                    dayNumber = 3,
                    themeTitle = "The Tiger of Mysore & River Sanctuaries",
                    slots = listOf(
                        ItinerarySlot(
                            timeSlot = "Morning (8:00 AM)",
                            activityTitle = "Cauvery Island Bird Safari",
                            placeName = "Ranganathittu Bird Sanctuary",
                            description = "Guided rowboat safari amidst colonies of painted storks, pelicans, spoonbills, and sunbathing marsh crocodiles.",
                            duration = "2.5 Hours",
                            travelTip = "Carry binoculars and telephoto lenses; morning light is ideal for photography.",
                            category = Category.NATURE
                        ),
                        ItinerarySlot(
                            timeSlot = "Midday (11:00 AM)",
                            activityTitle = "Teakwood Frescoes & Historic Dungeons",
                            placeName = "Srirangapatna (Dariya Daulat Bagh)",
                            description = "Explore Tipu Sultan's teak summer palace covered in battle murals, Bailey's Dungeon, and the historic fort ramparts.",
                            duration = "2.5 Hours",
                            travelTip = "Hire a certified monument guide at the gate to unearth the fascinating secrets of Mysore rocketry.",
                            category = Category.HERITAGE
                        ),
                        ItinerarySlot(
                            timeSlot = "Afternoon (2:00 PM)",
                            activityTitle = "Sacred River Sangama & Heritage Rice",
                            placeName = "Sangama & Sri Ranganathaswamy Temple",
                            description = "Marvel at the ancient 9th-century temple pillars and visit the confluence where three river streams embrace.",
                            duration = "2 Hours",
                            travelTip = "Sample local temple prasadam (curd rice and sweet pongal) in the agrahara.",
                            category = Category.CULTURE
                        ),
                        ItinerarySlot(
                            timeSlot = "Evening (5:30 PM)",
                            activityTitle = "Gokulam Cafe & Yoga Culture",
                            placeName = "Gokulam Artisan District",
                            description = "Relax in Mysore's bohemian district, home to global yoga schools, vegan bakeries, and organic cafes.",
                            duration = "2 Hours",
                            travelTip = "Check out local pottery studios and indie bookstores in 3rd stage Gokulam.",
                            category = Category.CULTURE
                        )
                    )
                )
            )
        }

        if (days >= 4) {
            // DAY 4+: Hoysala Marvels or Wildlife Expedition
            plans.add(
                GeneratedDayPlan(
                    dayNumber = 4,
                    themeTitle = "Hoysala Stone Wonders & Wildlife Excursion",
                    slots = listOf(
                        ItinerarySlot(
                            timeSlot = "Morning (8:00 AM)",
                            activityTitle = "UNESCO Hoysala Architectural Marvel",
                            placeName = "Somnathpur (Chennakeshava Temple)",
                            description = "Witness the breathtaking 13th-century trikuta temple with star-shaped plinths and stone filigree carvings of dancers and epics.",
                            duration = "3 Hours",
                            travelTip = "Located 35 km east; the peaceful rural drive passes sugarcane fields and jaggery crushers.",
                            category = Category.HERITAGE
                        ),
                        ItinerarySlot(
                            timeSlot = "Afternoon (1:00 PM)",
                            activityTitle = "Jaggery Tasting & Local Village Lunch",
                            placeName = "Bannur & T. Narasipura Route",
                            description = "Taste freshly boiled sugarcane juice, warm jaggery (Bella), and authentic regional Karnataka ragi mudde or akki rotti.",
                            duration = "1.5 Hours",
                            travelTip = "Ask local farmers to show you the traditional copper cauldron boiling process.",
                            category = Category.FOOD
                        ),
                        ItinerarySlot(
                            timeSlot = "Evening (4:00 PM)",
                            activityTitle = "Twin Roaring Waterfalls",
                            placeName = "Shivasamudram Falls (Gaganachukki)",
                            description = "Stand on the watchtower overlooking the mighty Cauvery plunging 300 feet over jagged rocky cliffs in two majestic cascades.",
                            duration = "2 Hours",
                            travelTip = "Monsoon to early winter offers the most thunderous water volume.",
                            category = Category.NATURE
                        ),
                        ItinerarySlot(
                            timeSlot = "Night (7:30 PM)",
                            activityTitle = "Farewell Royal Dinner",
                            placeName = "Spring Restaurant / Lalitha Mahal",
                            description = "Celebrate an unforgettable trip to the City of Palaces with royal recipes passed down by the Wadiyar court.",
                            duration = "2 Hours",
                            travelTip = "Don't forget to pack your boxes of GI-tagged Mysore Pak for loved ones back home!",
                            category = Category.STAY
                        )
                    )
                )
            )
        }

        return plans
    }
}
