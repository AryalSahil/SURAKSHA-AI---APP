package com.example.data

object LessonsData {
    val lessons: List<Lesson> = listOf(
        Lesson(
            id = "lesson_1",
            title = "How UPI Scams Work",
            category = "Payment Security",
            durationMinutes = 4,
            shortDescription = "Understand how UPI collect requests, deceptive QR codes, and fake refunds misdirect your funds.",
            whatIsThis = "UPI is designed for instant peer-to-peer transfers using a secure PIN. Fraudsters exploit confusion between sending and receiving money. In almost all UPI frauds, criminals convince you to authorize a debit transaction while claiming you are 'receiving' a cashback, prize, or refund.",
            fictionalExample = "Fictional Scenario: Ramesh posted an old sofa on an online marketplace. A prospective buyer called, agreed on ₹5,000 immediately, and said: 'I am sending a QR code on WhatsApp. Just scan it and type your UPI PIN to accept the payment.' Ramesh scanned the code, entered his PIN, and immediately received an SMS that ₹5,000 was debited from his account instead.",
            warningSigns = listOf(
                "Instructions to enter your UPI PIN to 'receive', 'claim', or 'verify' funds",
                "Receiving payment collect requests from unknown VPA handles",
                "QR codes sent via chat apps with instructions to scan them to receive money",
                "Requests to send ₹1 or ₹5 first as a 'test verification'"
            ),
            whatToDo = listOf(
                "Remember the golden rule: You NEVER enter your UPI PIN to receive money.",
                "Verify incoming money solely by checking your bank balance directly inside your app.",
                "Decline any unexpected 'Collect Request' notification immediately.",
                "Set strict daily UPI transaction limits in your banking settings."
            ),
            whatNotToDo = listOf(
                "Never scan a QR code sent by an unknown buyer on WhatsApp or Telegram.",
                "Never enter your UPI PIN on third-party links or pop-up prompts.",
                "Do not share your UPI registration SMS or mobile banking passcode."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "When do you need to enter your secret UPI PIN?",
                    options = listOf(
                        "Only when you are sending or transferring money out of your account",
                        "When someone wants to send you a cashback or refund",
                        "When verifying an incoming bank payment",
                        "When registering to receive a marketplace payment"
                    ),
                    correctIndex = 0,
                    explanation = "Entering your UPI PIN is always an authorization to debit funds from your account. You never need a PIN to receive incoming credits."
                ),
                QuizQuestion(
                    id = 2,
                    question = "A buyer sends you a QR code to receive money for an item you are selling. What should you do?",
                    options = listOf(
                        "Scan it immediately and enter your UPI PIN",
                        "Refuse to scan it — scanning QR codes with PIN entry debits your money",
                        "Forward the QR code to your friend to test it",
                        "Enter wrong PIN three times to test the buyer"
                    ),
                    correctIndex = 1,
                    explanation = "Scanning a QR code with PIN entry is exclusively used to pay out money. Legitimate buyers only need your UPI ID or phone number."
                ),
                QuizQuestion(
                    id = 3,
                    question = "What should you do if an unknown UPI 'Collect Request' appears on your phone?",
                    options = listOf(
                        "Approve it to see who sent it",
                        "Ignore it and hope it goes away",
                        "Decline or reject the request immediately",
                        "Enter 0000 as PIN"
                    ),
                    correctIndex = 2,
                    explanation = "Always decline unprompted collect requests. Approving them instantly transfers your money to the requester."
                )
            )
        ),
        Lesson(
            id = "lesson_2",
            title = "OTP and Account Takeover Scams",
            category = "Authentication",
            durationMinutes = 5,
            shortDescription = "Learn why One-Time Passwords are confidential and how fraudsters trick users into sharing them.",
            whatIsThis = "One-Time Passwords (OTPs) provide two-factor authentication to protect online banking, social media, and digital wallets. Scammers use urgent phone calls, SMS spoofing, and emotional manipulation to persuade victims into reading or forwarding these codes.",
            fictionalExample = "Fictional Scenario: Sunita received an automated-sounding call claiming her telecom SIM card was expiring in 30 minutes due to missing biometric updates. The caller said: 'We are sending a security verification code to your SMS. Read it out to avoid network blackout.' Panicking about missing urgent work calls, Sunita read out the 6-digit code. Seconds later, her WhatsApp was logged out and funds were moved from her linked wallet.",
            warningSigns = listOf(
                "Callers claiming to be from your bank, telecom company, or courier asking for a code",
                "SMS headers containing words like 'Do not share this OTP with anyone, including bank staff'",
                "Simultaneous password reset notifications on your email while receiving phone calls",
                "Pressure to disclose codes within seconds to prevent account closure"
            ),
            whatToDo = listOf(
                "Always read the full SMS text: note what action the OTP is authorizing (e.g. ₹10,000 debit vs login).",
                "Hang up on any caller who asks for a numeric code sent to your phone.",
                "Enable app-based authenticator tools or hardware keys where supported.",
                "Notify your bank immediately if you accidentally disclosed an authentication code."
            ),
            whatNotToDo = listOf(
                "Never share an OTP with bank managers, customer executives, or police personnel.",
                "Never forward authentication SMS messages to any phone number or email.",
                "Never type OTPs into forms accessed via unverified links sent in chats."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Does legitimate bank staff ever require your OTP over a phone call?",
                    options = listOf(
                        "Yes, for security verification during fraud checks",
                        "Yes, if they state their official employee ID",
                        "No, bank staff never require or request your OTP under any circumstance",
                        "Only if your card has expired"
                    ),
                    correctIndex = 2,
                    explanation = "Bank employees have backend administrative systems and have no operational reason to ever see or request your private OTP."
                ),
                QuizQuestion(
                    id = 2,
                    question = "What critical detail should you look for inside an OTP SMS?",
                    options = listOf(
                        "The exact amount and merchant name being authorized",
                        "The color of the SMS bubble",
                        "How many exclamation marks are used",
                        "The time zone of the server"
                    ),
                    correctIndex = 0,
                    explanation = "OTP messages clearly state the specific action (e.g., 'OTP for purchase of ₹15,000 at XYZ'). Reading this prevents authorizing fraudulent transactions."
                ),
                QuizQuestion(
                    id = 3,
                    question = "A caller states they need a code to stop a fraudulent transaction on your card. What should you do?",
                    options = listOf(
                        "Give them the code immediately to stop the fraud",
                        "Hang up and call the number on the back of your card independently",
                        "Ask them to call back in 5 minutes",
                        "Text the code to their personal mobile number"
                    ),
                    correctIndex = 1,
                    explanation = "Scammers often pretend to be fraud prevention agents. Hang up and dial your bank's verified helpline directly."
                )
            )
        ),
        Lesson(
            id = "lesson_3",
            title = "Fake Job Offers and Advance Fee Fraud",
            category = "Recruitment Fraud",
            durationMinutes = 4,
            shortDescription = "Spot bogus work-from-home offers, task-based Telegram scams, and upfront registration fee traps.",
            whatIsThis = "Recruitment scams lure job seekers with high salaries for low-effort tasks like data entry, YouTube video liking, or hotel reviews. After brief initial payouts, victims are asked to pay 'registration fees', 'security deposits', or 'tax clearance' to unlock their earnings.",
            fictionalExample = "Fictional Scenario: Ankit was added to a Telegram group offering ₹3,000 daily for liking travel videos. He completed 3 tasks and was actually sent ₹150 to gain his trust. Next, he was told to join a 'VIP tier' by depositing ₹2,000 to earn ₹5,000. After sending ₹2,000, the admin demanded ₹10,000 more for 'system tax clearance' and refused all withdrawals.",
            warningSigns = listOf(
                "Unsolicited job offers received via WhatsApp or Telegram from unknown numbers",
                "Promises of exorbitant income for trivial tasks (e.g. ₹5,000/day for liking videos)",
                "Demands for upfront money for registration, ID cards, training kits, or equipment",
                "Payments routed to individual personal UPI IDs rather than corporate accounts"
            ),
            whatToDo = listOf(
                "Research the company on official portals and verify open vacancies on their career page.",
                "Check whether official communications come from corporate domain emails (e.g., @company.com).",
                "Treat any upfront payment request for employment as an absolute red flag."
            ),
            whatNotToDo = listOf(
                "Never pay money to secure a job or interview slot.",
                "Do not participate in Telegram prepaid task schemes or investment groups.",
                "Do not share copies of your PAN or Aadhaar card before verifying employer legitimacy."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "A recruiter asks for a ₹1,500 refundable security deposit for a work-from-home laptop. Is this legitimate?",
                    options = listOf(
                        "Yes, all companies require security deposits for equipment",
                        "No, legitimate employers provide work equipment without upfront fees",
                        "Yes, as long as they provide a receipt",
                        "Yes, if they promise to return it with the first salary"
                    ),
                    correctIndex = 1,
                    explanation = "Legitimate companies never demand upfront cash or deposits from candidates as a condition of employment."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Why do task-based scam groups pay a small amount (like ₹100 or ₹200) at the start?",
                    options = listOf(
                        "Because they are generous employers",
                        "To establish false trust so victims deposit much larger amounts later",
                        "To comply with labor laws",
                        "To test the banking system"
                    ),
                    correctIndex = 1,
                    explanation = "Initial micro-payouts are psychological bait to lower defenses before soliciting thousands of rupees in unrecoverable deposits."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Which communication channel is commonly misused by recruitment fraudsters?",
                    options = listOf(
                        "Official corporate email servers ending in @companyname.com",
                        "Unsolicited WhatsApp or Telegram messages from international numbers",
                        "Official verified company portal postings",
                        "Campus placement drives"
                    ),
                    correctIndex = 1,
                    explanation = "Reputable companies do not conduct hiring exclusively through unsolicited messaging apps using overseas numbers."
                )
            )
        ),
        Lesson(
            id = "lesson_4",
            title = "Fake Loan Applications",
            category = "Lending Traps",
            durationMinutes = 4,
            shortDescription = "Recognize illegal loan apps, zero-document approvals, and extortion-based lending software.",
            whatIsThis = "Predatory lending schemes offer instant loans without credit checks or documentation. These apps demand broad phone permissions (contacts, gallery, camera) during installation, disburse only a fraction of the promised loan, and use harassment to extort exorbitant payments.",
            fictionalExample = "Fictional Scenario: Neha needed urgent cash and downloaded an instant loan app advertised on social media. The app requested permission to access her contact list and photos. She applied for ₹20,000; the app deducted ₹8,000 as processing fees and credited only ₹12,000. Within four days, recovery agents began calling her parents and colleagues with abusive threats.",
            warningSigns = listOf(
                "Instant loan approval with zero income proof or credit score verification",
                "App requesting full access to your phone contacts, photo gallery, and storage",
                "Upfront 'processing fee' demanded before the loan amount is released",
                "Lending entity not registered with the Reserve Bank of India (RBI)"
            ),
            whatToDo = listOf(
                "Verify that the lending partner is an RBI-registered NBFC or regulated bank.",
                "Check the RBI Sachet portal (sachet.rbi.org.in) before applying.",
                "Review app permissions carefully and deny access to contacts and personal media."
            ),
            whatNotToDo = listOf(
                "Never install loan applications distributed via APK links outside official app stores.",
                "Never pay advance disbursement or insurance fees to unlock a loan.",
                "Do not yield to blackmail; report extortion directly to the cybercrime cell."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Why do illegal loan apps demand permission to access your mobile contacts and photos?",
                    options = listOf(
                        "To automatically back up your family pictures",
                        "To harass and blackmail your contacts if you dispute illegal interest rates",
                        "To improve their artificial intelligence models",
                        "To verify your home address"
                    ),
                    correctIndex = 1,
                    explanation = "Predatory lenders harvest contact books and photos to shame victims by threatening to message friends and family."
                ),
                QuizQuestion(
                    id = 2,
                    question = "An online agent claims a ₹3,00,000 loan is approved, but you must pay ₹3,500 'file charge' first. What is this?",
                    options = listOf(
                        "Standard banking procedure",
                        "An advance-fee scam — real banks deduct fees from the disbursed loan",
                        "A government tax requirement",
                        "A guarantee bond"
                    ),
                    correctIndex = 1,
                    explanation = "Regulated lenders deduct legitimate processing fees directly from the approved loan disbursement, never requiring upfront transfers."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Where can you verify whether a digital lender is legitimately registered in India?",
                    options = listOf(
                        "On random social media comments",
                        "On the Reserve Bank of India (RBI) list of registered NBFCs and Sachet portal",
                        "By asking the loan telecaller",
                        "On an unverified blog post"
                    ),
                    correctIndex = 1,
                    explanation = "The RBI maintains an authoritative public registry of all licensed banks and Non-Banking Financial Companies (NBFCs)."
                )
            )
        ),
        Lesson(
            id = "lesson_5",
            title = "Phishing Links and Fake Websites",
            category = "Web Safety",
            durationMinutes = 5,
            shortDescription = "Learn how to inspect website URLs, identify spoofed domains, and avoid credential harvesting pages.",
            whatIsThis = "Phishing involves crafting fraudulent websites that replicate the exact visual design of banks, tax departments, or utility providers. The objective is to trick visitors into typing usernames, netbanking passwords, and credit card details.",
            fictionalExample = "Fictional Scenario: Vikram received an SMS: 'Your electricity power will be cut tonight at 9:30 PM due to unpaid bill. Update bill immediately at http://power-bijli-update.xyz/pay'. Panicked by the thought of his power being disconnected, he clicked the link. The page had a convincing electricity board logo and asked for his net banking login and password.",
            warningSigns = listOf(
                "Domains ending in unusual extensions like .xyz, .tk, .ga, .cf, or .top",
                "Brand names combined with hyphenated terms (e.g. sbi-kyc-verify.com)",
                "Urgent threats of power disconnection, tax fines, or bank account freezing",
                "HTTP links lacking secure HTTPS encryption (though HTTPS alone does not guarantee safety)"
            ),
            whatToDo = listOf(
                "Inspect the root domain carefully before typing any passwords.",
                "Remember that Indian government portals always end with '.gov.in' or '.nic.in'.",
                "Bookmark official banking and utility websites rather than clicking chat links.",
                "Use the SURAKSHA AI link checker to inspect suspicious URLs."
            ),
            whatNotToDo = listOf(
                "Never click links sent in SMS alerts regarding bill defaults or lottery wins.",
                "Never enter payment PINs or netbanking passwords on pages opened from messaging apps.",
                "Do not trust a site solely because it displays a padlock icon; free SSL certificates are used by scammers too."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Which of the following domain names is the legitimate official Indian government portal?",
                    options = listOf(
                        "incometax-india-efiling.xyz",
                        "incometax.gov.in",
                        "incometax-portal-update.com",
                        "gov-incometax.net"
                    ),
                    correctIndex = 1,
                    explanation = "All authentic Indian central and state government departments utilize the reserved '.gov.in' or '.nic.in' domains."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Does a green padlock or 'https://' mean a website is guaranteed safe from scams?",
                    options = listOf(
                        "Yes, hackers can never get an SSL padlock",
                        "No, HTTPS only means the connection is encrypted; scammers also use HTTPS on phishing sites",
                        "Yes, padlocks are issued only after police verification",
                        "Only if the website is in English"
                    ),
                    correctIndex = 1,
                    explanation = "HTTPS secures data in transit, but fraudulent sites can easily obtain free SSL certificates. You must verify the actual domain name."
                ),
                QuizQuestion(
                    id = 3,
                    question = "What should you do if an SMS threatens that your power bill is overdue and supplies an update link?",
                    options = listOf(
                        "Click the link immediately to pay",
                        "Ignore the link and check your bill on the official state electricity board portal or app",
                        "Forward the link to your neighbours",
                        "Reply with your customer ID"
                    ),
                    correctIndex = 1,
                    explanation = "Utility providers do not issue disconnection threats via generic mobile links. Always check through official utility portals."
                )
            )
        ),
        Lesson(
            id = "lesson_6",
            title = "Fake Customer Care Numbers",
            category = "Search Engine Manipulation",
            durationMinutes = 4,
            shortDescription = "How scammers place fake helpline numbers on Google Maps and search results to trap callers.",
            whatIsThis = "When users search online for customer care numbers for courier companies, airlines, or payment apps, scammers manipulate Google search snippets and Google Maps listings with their personal mobile numbers. When called, they pose as executives and instruct users to transfer funds or share screen access.",
            fictionalExample = "Fictional Scenario: Kavita was waiting for an e-commerce refund of ₹800. She searched 'courier customer care number' on Google and dialed the first number listed in the search results. The person who answered politely apologized for the delay and asked her to download AnyDesk so they could verify her transaction reference on her phone.",
            warningSigns = listOf(
                "Customer service numbers that are ordinary 10-digit mobile phone numbers rather than toll-free 1800 numbers",
                "Executives asking you to download screen sharing tools to resolve refund issues",
                "Demands to send nominal amounts (e.g. ₹2) to 'initiate refund routing'",
                "The person calling back repeatedly from different personal numbers"
            ),
            whatToDo = listOf(
                "Obtain customer support contact information exclusively from within the official installed mobile app.",
                "Look for official verified toll-free numbers (often starting with 1800).",
                "Contact e-commerce and courier support through in-app ticketing systems."
            ),
            whatNotToDo = listOf(
                "Never trust customer care numbers discovered on open web search pages or Google Maps photos.",
                "Never install remote management tools at the behest of customer service personnel.",
                "Never provide card CVVs or bank PINs to a customer service agent."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Where is the safest place to find genuine customer support for a banking or shopping app?",
                    options = listOf(
                        "Google Search top results",
                        "Inside the 'Help & Support' section of the officially installed app",
                        "In public comments on social media",
                        "In unverified WhatsApp groups"
                    ),
                    correctIndex = 1,
                    explanation = "In-app support sections are maintained directly by the organization and cannot be easily tampered with by external fraudsters."
                ),
                QuizQuestion(
                    id = 2,
                    question = "A courier support agent on a phone call asks you to install AnyDesk to track your package. What is your response?",
                    options = listOf(
                        "Install it immediately so they can locate the parcel",
                        "Refuse and disconnect the call immediately; delivery support never requires remote screen control",
                        "Give them your computer password instead",
                        "Install it on a family member's phone"
                    ),
                    correctIndex = 1,
                    explanation = "Package tracking happens on company logistics servers. Demanding remote phone access is an explicit indicator of an account draining attempt."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Is a standard 10-digit mobile number typically an official national helpline for a major airline or bank?",
                    options = listOf(
                        "Yes, banks prefer personal cell phones",
                        "No, legitimate national institutions utilize toll-free numbers (1800-xxx-xxxx) or verified institutional routing",
                        "Yes, if the agent speaks politely",
                        "Only on weekends"
                    ),
                    correctIndex = 1,
                    explanation = "National institutions use registered toll-free or centralized PBX lines, not personal mobile phone numbers."
                )
            )
        ),
        Lesson(
            id = "lesson_7",
            title = "Remote Access App Scams (AnyDesk, TeamViewer)",
            category = "Device Security",
            durationMinutes = 5,
            shortDescription = "Understand how screen-sharing applications grant criminals total visibility and control over your phone.",
            whatIsThis = "Legitimate diagnostic applications like AnyDesk, TeamViewer, and QuickSupport permit remote viewing of computer and mobile screens. Scammers exploit these tools by convincing victims that remote access is necessary to fix technical errors or process refunds, allowing them to intercept OTPs and empty accounts.",
            fictionalExample = "Fictional Scenario: Manoj received a call from an alleged telecom engineer stating his 5G network settings needed remote calibration. The engineer guided Manoj to install QuickSupport from the Play Store and read out the 9-digit session code. While the screen was mirrored, the caller instructed Manoj to open his banking app to verify connection. The caller recorded his credentials and initiated unauthorized wire transfers.",
            warningSigns = listOf(
                "Any caller requesting you to install AnyDesk, TeamViewer, QuickSupport, or RustDesk",
                "Requests to read aloud a 9-digit or 10-digit session or connection code",
                "Instructions to keep your mobile screen active while an unknown caller operates your device",
                "Warnings from Android about screen recording permissions appearing during a call"
            ),
            whatToDo = listOf(
                "Uninstall any screen-sharing application immediately if downloaded under pressure.",
                "Turn on Airplane Mode or disconnect Wi-Fi and mobile data immediately if unauthorized control is initiated.",
                "Read system permission prompts carefully before tapping 'Start Now' on screen capture dialogs."
            ),
            whatNotToDo = listOf(
                "Never share your remote access connection ID or invite code with an unknown party.",
                "Never log in to banking or payment apps while any screen-sharing session is active.",
                "Do not ignore Android's warning dialog: 'This app can capture everything displayed on your screen'."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "What can someone see when you grant them access through a screen-sharing app like AnyDesk on your phone?",
                    options = listOf(
                        "Only your network speed",
                        "Everything visible on your screen, including OTPs, passwords, and banking apps",
                        "Nothing without signing a contract",
                        "Only your wallpaper"
                    ),
                    correctIndex = 1,
                    explanation = "Screen-sharing tools broadcast everything on your phone screen in real time, exposing confidential SMS codes and typed passwords."
                ),
                QuizQuestion(
                    id = 2,
                    question = "If you realize you have mistakenly allowed remote access to an unknown caller, what should you do first?",
                    options = listOf(
                        "Ask the caller politely to stop",
                        "Immediately turn on Airplane Mode or power off the phone to sever connection, then uninstall the app",
                        "Open your bank app to transfer money out",
                        "Wait for the call to finish"
                    ),
                    correctIndex = 1,
                    explanation = "Cutting off network connectivity immediately severs the attacker's remote stream, preventing further unauthorized actions."
                ),
                QuizQuestion(
                    id = 3,
                    question = "Do legitimate bank executives ever require remote screen sharing to solve account problems?",
                    options = listOf(
                        "Yes, for senior citizens",
                        "No, bank policies strictly forbid employees from accessing customer devices remotely",
                        "Only during bank holidays",
                        "Yes, if they provide a case reference number"
                    ),
                    correctIndex = 1,
                    explanation = "Financial institutions never require remote control of customer devices to manage banking issues."
                )
            )
        ),
        Lesson(
            id = "lesson_8",
            title = "Impersonation and Social Engineering",
            category = "Social Engineering",
            durationMinutes = 4,
            shortDescription = "Recognize digital arrest threats, fake police calls, and customs parcel extortion tactics.",
            whatIsThis = "Social engineering manipulates human psychology rather than technical flaws. Scammers pose as police officers, customs officials, CBI agents, or judges on video calls, threatening immediate arrest or confiscation of contraband parcels to extort money into 'safe government escrow accounts'.",
            fictionalExample = "Fictional Scenario: Deepa received a Skype video call from an individual in a police uniform sitting in front of a bogus police banner. The caller claimed an international parcel containing illegal drugs in her name was intercepted in Mumbai, and an arrest warrant was active. Terrified, Deepa was told she must transfer ₹2,50,000 to an 'RBI verification account' to prove innocence.",
            warningSigns = listOf(
                "Video calls on Skype, WhatsApp, or Telegram featuring individuals in police uniforms",
                "Claims of 'Digital Arrest' or orders forbidding you from leaving your home or disconnecting the call",
                "Allegations that a parcel containing passports or narcotics in your name was seized",
                "Demands to transfer money to a private bank account for 'legal clearance' or 'fund auditing'"
            ),
            whatToDo = listOf(
                "Remember: Indian law and police procedure does NOT have any concept of 'Digital Arrest'.",
                "Disconnect the call immediately and approach your nearest local police station.",
                "Call 1930 to report fake police calls or impersonation threats.",
                "Speak to a trusted family member or lawyer before making any decisions."
            ),
            whatNotToDo = listOf(
                "Never transfer funds to any account to 'prove innocence' or clear a police case.",
                "Do not isolate yourself or follow instructions to keep the investigation secret from family.",
                "Never display your identity documents or home on unverified video calls."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "Does the legal system or police force in India conduct 'Digital Arrest' via WhatsApp or Skype video calls?",
                    options = listOf(
                        "Yes, it was introduced for modern cyber enforcement",
                        "No, 'Digital Arrest' is completely fictitious and used exclusively by scammers",
                        "Only for international parcels",
                        "Yes, by the Supreme Court"
                    ),
                    correctIndex = 1,
                    explanation = "Law enforcement agencies follow statutory judicial procedures. Police never arrest citizens over video calls or demand financial deposits."
                ),
                QuizQuestion(
                    id = 2,
                    question = "A caller claiming to be a customs officer demands ₹50,000 to drop charges for a seized package. What is happening?",
                    options = listOf(
                        "A genuine legal clearance procedure",
                        "An extortion scam — government agencies never settle criminal matters via personal bank transfers",
                        "A courier customs handling fee",
                        "An import tax duty"
                    ),
                    correctIndex = 1,
                    explanation = "Government agencies do not demand private bank deposits under threats of immediate arrest."
                ),
                QuizQuestion(
                    id = 3,
                    question = "What should you do if an aggressive caller threatens to send police to your house within an hour?",
                    options = listOf(
                        "Send whatever money they ask for",
                        "Stay calm, disconnect, and call the official emergency police helpline 112 or visit your local police station",
                        "Run away from home",
                        "Turn off your phone for a month"
                    ),
                    correctIndex = 1,
                    explanation = "Urgency and aggression are designed to induce panic. Contacting local authorities through verified channels neutralizes the threat."
                )
            )
        ),
        Lesson(
            id = "lesson_9",
            title = "Safe Online Shopping and Payments",
            category = "Consumer Protection",
            durationMinutes = 4,
            shortDescription = "Learn how to spot counterfeit e-commerce websites, fake social media storefronts, and prepaid traps.",
            whatIsThis = "Counterfeit shopping sites and scam social media ads advertise luxury electronics, designer apparel, or branded footwear at 80% to 90% discounts. Buyers make upfront payments via UPI or card, but the items never arrive, and customer support channels turn out to be fake.",
            fictionalExample = "Fictional Scenario: Rahul saw an Instagram advertisement offering the latest flagship smartphone for ₹9,999 (regular price ₹70,000) under a 'clearance festival'. The link opened a store styled like a major retailer. Rahul paid using UPI. He received no order confirmation, the Instagram page was deleted the next day, and the website disappeared.",
            warningSigns = listOf(
                "Prices that are impossibly cheap (e.g. 70-90% discount on newly launched electronics)",
                "Storefronts that only accept prepaid UPI payments and offer no Cash on Delivery (COD)",
                "Websites with no registered physical address, company details, or verified contact information",
                "New social media accounts with disabled comments and stock product photography"
            ),
            whatToDo = listOf(
                "Look up reviews on independent consumer review platforms before buying from unfamiliar shops.",
                "Choose Cash on Delivery (COD) on unfamiliar platforms for your initial order.",
                "Check the domain registration age on WHOIS tools — scam sites are typically only days old."
            ),
            whatNotToDo = listOf(
                "Do not trust sponsored social media advertisements blindly without independent verification.",
                "Never transfer money directly to a seller's personal bank account or personal UPI ID.",
                "Do not click shortened links in social media bio sections."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "A newly discovered online shop offers a brand new ₹60,000 laptop for ₹6,500. What is the most probable reality?",
                    options = listOf(
                        "An extraordinary festival clearance discount",
                        "A scam designed to steal your upfront payment",
                        "A government subsidized program",
                        "Surplus stock release"
                    ),
                    correctIndex = 1,
                    explanation = "Prices far below wholesale cost are standard indicators of fraudulent stores that never fulfill orders."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Why is Cash on Delivery (COD) recommended when shopping on an unfamiliar website for the first time?",
                    options = listOf(
                        "Because credit cards are illegal online",
                        "Because it ensures you pay only after the package physically reaches your hands",
                        "Because delivery drivers give discounts",
                        "To save on battery"
                    ),
                    correctIndex = 1,
                    explanation = "COD mitigates the risk of non-delivery from phantom storefronts that vanish after taking prepaid funds."
                ),
                QuizQuestion(
                    id = 3,
                    question = "What should you check if an unfamiliar online brand claims to have an office in your city?",
                    options = listOf(
                        "Look up the physical address on Google Maps and check official registration details",
                        "Assume it must be true if printed on the webpage",
                        "Ask on an anonymous forum",
                        "Check only their logo design"
                    ),
                    correctIndex = 0,
                    explanation = "Verifying physical existence and regulatory details helps weed out fictional shell entities."
                )
            )
        ),
        Lesson(
            id = "lesson_10",
            title = "What To Do After a Cyber Fraud Incident",
            category = "Incident Response",
            durationMinutes = 5,
            shortDescription = "Crucial immediate steps to take if you have lost money or compromised confidential credentials.",
            whatIsThis = "When financial cyber fraud occurs, the first 2 to 3 hours — often referred to by law enforcement as the 'Golden Hour' — are vital. Promptly reporting the fraud to the 1930 helpline and your bank enables authorities to freeze the stolen funds before they are withdrawn through ATM mules.",
            fictionalExample = "Fictional Scenario: Aruna realized she was duped out of ₹40,000 through a fake electricity bill link. Instead of waiting till morning, she immediately called 1930 within 25 minutes. She gave the officer the exact transaction UTR number and the fraudster's UPI ID. The nodal officer alerted the receiving bank, which successfully froze the account before the fraudster could cash out.",
            warningSigns = listOf(
                "Unexpected SMS debit alert for transactions you did not authorize",
                "Your mobile SIM card abruptly losing network while receiving suspicious alerts (SIM swap sign)",
                "Sudden logout from your banking or digital wallet applications",
                "Inability to reach contacts or incoming calls being forwarded without your knowledge"
            ),
            whatToDo = listOf(
                "Call 1930 (National Cyber Crime Helpline) immediately — every minute counts.",
                "Call the customer care number on the back of your bank card to block the card, UPI, and net banking.",
                "Preserve all transaction IDs, UTR references, SMS alerts, and call records.",
                "File an official formal complaint on the National Cyber Crime Portal at cybercrime.gov.in.",
                "Change all your passwords from a clean, secure device."
            ),
            whatNotToDo = listOf(
                "Do not delete the scammer's text messages, WhatsApp chats, or call logs — they are crucial evidence.",
                "Do not wait until the next working day; the 1930 helpline operates 24/7 across India.",
                "Do not hire private 'hackers' on Instagram promising to recover your stolen funds — they are secondary scammers."
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    question = "What is the official 24/7 National Cyber Crime helpline number in India for financial fraud?",
                    options = listOf(
                        "100",
                        "1930",
                        "108",
                        "139"
                    ),
                    correctIndex = 1,
                    explanation = "1930 is the dedicated national cybercrime citizen helpline managed by the Ministry of Home Affairs and I4C."
                ),
                QuizQuestion(
                    id = 2,
                    question = "Why is reporting to 1930 within the 'Golden Hour' (first 2-3 hours) critical?",
                    options = listOf(
                        "Because the helpline closes after 3 hours",
                        "Because banks can trace and freeze the stolen funds before fraudsters withdraw cash at ATMs",
                        "Because police only work during golden hours",
                        "To qualify for an insurance prize"
                    ),
                    correctIndex = 1,
                    explanation = "Quick intervention allows nodal banking coordinators to place an administrative hold on intermediary accounts before the money is dispersed."
                ),
                QuizQuestion(
                    id = 3,
                    question = "You see an account on social media claiming 'We recover lost cyber fraud money in 24 hours for a 10% fee'. What should you do?",
                    options = listOf(
                        "Pay their fee to recover your money quickly",
                        "Avoid them completely — they are recovery scammers preying on victims",
                        "Share your bank statements with them",
                        "Recommend them to other victims"
                    ),
                    correctIndex = 1,
                    explanation = "Secondary recovery scams target already vulnerable victims. Only official police and banking channels can freeze and restore defrauded funds."
                )
            )
        )
    )
}
