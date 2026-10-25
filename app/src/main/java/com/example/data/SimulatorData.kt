package com.example.data

object SimulatorData {
    val scenarios: List<Scenario> = listOf(
        // Scenario A: Fake Job Offer
        Scenario(
            id = "scenario_fake_job",
            title = "Work-From-Home Recruitment Trap",
            category = ScenarioCategory.FAKE_JOB,
            difficulty = ScenarioDifficulty.BEGINNER,
            estimatedMinutes = 3,
            shortDescription = "An unexpected recruiter offers high daily pay for basic tasks, then demands an upfront security deposit.",
            contextDescription = "You receive an unsolicited WhatsApp message from an unknown number claiming to be a senior recruiter from a reputable digital media agency.",
            initialStepId = "job_step_1",
            relatedLessonId = "lesson_3",
            learningTakeaway = "Legitimate companies never ask applicants to pay recruitment, training, or equipment fees. Unsolicited high-paying jobs via chat apps are almost always advance-fee scams.",
            safetyChecklist = listOf(
                "Never transfer money or deposits to secure any job or equipment.",
                "Verify official vacancies directly on the company's verified careers webpage.",
                "Check for official corporate email domains (@company.com) rather than generic free email or chat handles.",
                "Be suspicious of unusually high compensation offered for minimal or entry-level skills."
            ),
            commonMistakeToAvoid = "Paying a small 'refundable fee' hoping the opportunity is genuine. Scammers use initial small payments to lock you into escalating demands.",
            safestAction = "Stop communication immediately. Block the sender and report the contact to the National Cyber Crime Reporting Portal at cybercrime.gov.in or call 1930.",
            steps = mapOf(
                "job_step_1" to ScenarioStep(
                    id = "job_step_1",
                    stepNumber = 1,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Sneha - Apex Talent HR",
                        text = "Hello! Congratulations! Your resume has been shortlisted for an exclusive Work-From-Home Content Evaluation role. You can earn ₹2,500 to ₹4,000 daily for just 1-2 hours of simple review tasks. Are you interested in starting today?",
                        timestampText = "10:15 AM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Unsolicited Messaging App Outreach",
                            description = "Official recruiters do not reach out out-of-the-blue on WhatsApp or Telegram from unknown mobile numbers.",
                            detectedInStep = 1
                        ),
                        RedFlagItem(
                            title = "Unrealistic Pay for Minimal Skill",
                            description = "Earning ₹2,500-₹4,000 daily for 1 hour of basic tasks is a classic lure to trigger excitement and lower skepticism.",
                            detectedInStep = 1
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_job_1_safe",
                            text = "Where did you receive my application, and what is your official company email address?",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Excellent response! Demanding proof of application and official corporate domains immediately challenges the impersonation.",
                            nextStepId = "job_step_2",
                            explanation = "Legitimate recruiters can readily provide corporate email domains and reference real vacancy requisitions."
                        ),
                        ScenarioOption(
                            id = "opt_job_1_caution",
                            text = "Please share the complete job description, company registration ID, and official website first.",
                            isSafe = true,
                            pointsDelta = 10,
                            feedbackText = "Good caution. Requesting verifiable employer credentials helps establish if the entity actually exists.",
                            nextStepId = "job_step_2",
                            explanation = "Always verify the company's registration on the Ministry of Corporate Affairs (MCA) portal before proceeding."
                        ),
                        ScenarioOption(
                            id = "opt_job_1_unsafe",
                            text = "Wow, ₹4,000 a day sounds amazing! What do I need to do right now to confirm my spot?",
                            isSafe = false,
                            pointsDelta = -10,
                            feedbackText = "Risky enthusiasm! Showing immediate eagerness signals to the scammer that you have swallowed the bait.",
                            nextStepId = "job_step_2",
                            explanation = "Expressing unverified excitement makes fraudsters accelerate their payment demands.",
                            mistakeTag = "Fell for high-pay bait"
                        )
                    )
                ),
                "job_step_2" to ScenarioStep(
                    id = "job_step_2",
                    stepNumber = 2,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Sneha - Apex Talent HR",
                        text = "We are registered with Apex Marketing Ltd. To activate your employee portal and dispatch your training welcome kit, you must deposit a one-time refundable registration fee of ₹1,200 to our payroll desk UPI: apextalent.pay@okaxis. This will be reimbursed in your first week's salary.",
                        timestampText = "10:18 AM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Upfront Fee Requirement",
                            description = "Demanding upfront money under the guise of registration, onboarding, or training kits is the hallmark of advance-fee fraud.",
                            detectedInStep = 2
                        ),
                        RedFlagItem(
                            title = "Personal or Generic UPI Handle",
                            description = "Official corporate payments go through regulated merchant gateways, not generic ad-hoc UPI IDs.",
                            detectedInStep = 2
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_job_2_safe",
                            text = "Legitimate companies never demand upfront fees from job candidates. I refuse to pay any money.",
                            isSafe = true,
                            pointsDelta = 20,
                            feedbackText = "Outstanding! Calling out the upfront fee rule directly dismantles the scam premise.",
                            nextStepId = "job_step_3",
                            explanation = "Standard corporate HR practice never charges applicants for registration or work materials."
                        ),
                        ScenarioOption(
                            id = "opt_job_2_unsafe_pay",
                            text = "Okay, if it's 100% refundable with my first salary, I will pay the ₹1,200 via UPI now.",
                            isSafe = false,
                            pointsDelta = -25,
                            feedbackText = "High Danger! Once transferred, advance fees are never returned, and scammers will demand even larger amounts.",
                            nextStepId = "job_step_3",
                            explanation = "The promise of 'reimbursement in first salary' is a fictional hook to lower financial resistance.",
                            mistakeTag = "Agreed to upfront registration fee"
                        ),
                        ScenarioOption(
                            id = "opt_job_2_negotiate",
                            text = "Can you deduct the ₹1,200 directly from my first week's payout instead of me paying in advance?",
                            isSafe = false,
                            pointsDelta = 0,
                            feedbackText = "While you avoided paying cash immediately, negotiating keeps you engaged with a criminal enterprise.",
                            nextStepId = "job_step_3",
                            explanation = "Scammers cannot deduct from nonexistent salaries; they will create artificial excuses to demand upfront cash anyway.",
                            mistakeTag = "Engaged in fee negotiation"
                        )
                    )
                ),
                "job_step_3" to ScenarioStep(
                    id = "job_step_3",
                    stepNumber = 3,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Sneha - Apex Talent HR",
                        text = "URGENT: Only 2 slots are left for your state! If you don't transfer the ₹1,200 within the next 15 minutes, your slot will be reassigned and your profile will be blacklisted across all partner job networks!",
                        timestampText = "10:21 AM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Artificial Urgency & Time Pressure",
                            description = "A 15-minute countdown forces emotional reaction instead of rational analysis.",
                            detectedInStep = 3
                        ),
                        RedFlagItem(
                            title = "Coercive Threats & Blacklisting",
                            description = "Threatening to blacklist candidates for not paying an unverified fee is blatant psychological manipulation.",
                            detectedInStep = 3
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_job_3_safe",
                            text = "Artificial urgency and threats confirm this is a scam. I am blocking this number and filing a cybercrime report.",
                            isSafe = true,
                            pointsDelta = 20,
                            feedbackText = "Spot on! Recognizing manufactured urgency as an attack signal protects you and your funds.",
                            nextStepId = "job_step_4",
                            explanation = "High-pressure deadlines are intended to bypass critical thinking and prevent independent verification."
                        ),
                        ScenarioOption(
                            id = "opt_job_3_panic",
                            text = "Please don't blacklist me! I am opening my UPI app right now to pay, please hold my spot!",
                            isSafe = false,
                            pointsDelta = -25,
                            feedbackText = "Critical Mistake! Yielding to panic and threat of blacklisting guarantees immediate financial loss.",
                            nextStepId = "job_step_4",
                            explanation = "Blacklist threats from messaging app numbers are completely baseless and unenforceable.",
                            mistakeTag = "Succumbed to artificial urgency pressure"
                        ),
                        ScenarioOption(
                            id = "opt_job_3_stall",
                            text = "Please give me until evening so I can consult my family about this fee.",
                            isSafe = true,
                            pointsDelta = 5,
                            feedbackText = "Seeking second opinions is healthy, but delaying gives the fraudster more opportunities to manipulate you.",
                            nextStepId = "job_step_4",
                            explanation = "Immediate disengagement is much safer than negotiating additional time with a fraudster."
                        )
                    )
                ),
                "job_step_4" to ScenarioStep(
                    id = "job_step_4",
                    stepNumber = 4,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SYSTEM_NOTE,
                        speakerName = "SURAKSHA Safety Advisor",
                        text = "Final Action: You have identified several warning signs (unsolicited message, unrealistic compensation, upfront fee, artificial urgency, blacklist threats). What is your definitive next step?",
                        timestampText = "10:23 AM",
                        isSuspiciousFlagged = false
                    ),
                    redFlagsAtThisStep = emptyList(),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_job_4_safe",
                            text = "Block the number, report the chat on WhatsApp, and log the incident on cybercrime.gov.in or 1930.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Perfect ending! Reporting and blocking breaks the fraud pipeline and alerts community defenses.",
                            nextStepId = null,
                            explanation = "Reporting UPI handles and phone numbers helps law enforcement freeze scam infrastructure."
                        ),
                        ScenarioOption(
                            id = "opt_job_4_unsafe",
                            text = "Forward the job opportunity to friends and college groups in case someone else wants to take the risk.",
                            isSafe = false,
                            pointsDelta = -30,
                            feedbackText = "Severe Risk! Forwarding known scams exposes your friends and contacts to financial harm.",
                            nextStepId = null,
                            explanation = "Never amplify unverified or suspicious job solicitations into social networks.",
                            mistakeTag = "Amplified scam to friends and family"
                        )
                    )
                )
            )
        ),

        // Scenario B: Bank Impersonation and OTP Scam
        Scenario(
            id = "scenario_bank_otp",
            title = "Bank Impersonation & OTP Extortion",
            category = ScenarioCategory.BANK_IMPERSONATION,
            difficulty = ScenarioDifficulty.BEGINNER,
            estimatedMinutes = 4,
            shortDescription = "A caller poses as a bank fraud investigator claiming your card was compromised, demanding your secret OTP.",
            contextDescription = "You receive an alarming phone call and SMS alert claiming an unauthorized withdrawal of ₹48,000 is pending on your bank account.",
            initialStepId = "otp_step_1",
            relatedLessonId = "lesson_2",
            learningTakeaway = "Bank employees never ask for OTPs, CVVs, ATM PINs, or net banking passwords under any circumstance. OTPs authorize debits or logins, never cancellations.",
            safetyChecklist = listOf(
                "Never disclose an OTP to anyone — including people claiming to be bank managers or police.",
                "Always read the full SMS text: look for the authorized amount and merchant name before doing anything.",
                "To verify account status, hang up and dial the official customer care number printed on your physical card.",
                "Call 1930 immediately if you ever accidentally disclose a one-time passcode."
            ),
            commonMistakeToAvoid = "Believing that reading out an OTP will 'cancel' or 'reverse' a pending fraud. OTPs authorize new transactions, they never cancel them.",
            safestAction = "Hang up immediately. Do not share any code. Check account balance directly inside your official banking app and call the number on your debit card.",
            steps = mapOf(
                "otp_step_1" to ScenarioStep(
                    id = "otp_step_1",
                    stepNumber = 1,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "SBI Fraud Prevention Desk",
                        text = "ALERT: Unauthorized transaction of ₹48,500 detected on your debit card at Electronic City, Bengaluru. If this was NOT you, reply 'CANCEL' or speak to Officer Rajesh immediately to halt payment within 10 minutes.",
                        timestampText = "02:14 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "High-Anxiety Fear Tactic",
                            description = "Fabricating a huge unauthorized transaction induces instant panic to impair judgment.",
                            detectedInStep = 1
                        ),
                        RedFlagItem(
                            title = "Strict Countdown Timer",
                            description = "Imposing a 10-minute window prevents you from calling your actual local branch manager.",
                            detectedInStep = 1
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_otp_1_safe",
                            text = "Do not reply to the SMS. Open my official banking app independently to inspect my statement.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Ideal response! Verifying through your own bookmarked app bypasses spoofed communication channels.",
                            nextStepId = "otp_step_2",
                            explanation = "Independent app verification immediately exposes whether any actual transaction occurred."
                        ),
                        ScenarioOption(
                            id = "opt_otp_1_caution",
                            text = "Call the verified toll-free customer care number printed on the back of my physical debit card.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Safe and proactive! Physical bank cards carry verified, tamper-resistant contact telephone lines.",
                            nextStepId = "otp_step_2",
                            explanation = "Never call numbers supplied in an unsolicited alert; always use the physical card number."
                        ),
                        ScenarioOption(
                            id = "opt_otp_1_unsafe",
                            text = "Call back the sender number in panic: 'Please cancel the ₹48,500 debit right away!'",
                            isSafe = false,
                            pointsDelta = -15,
                            feedbackText = "Dangerous! Calling back connects you directly into the scammer's boiler-room operation.",
                            nextStepId = "otp_step_2",
                            explanation = "The sender number is controlled by the attacker posing as bank personnel.",
                            mistakeTag = "Called back the scammer directly"
                        )
                    )
                ),
                "otp_step_2" to ScenarioStep(
                    id = "otp_step_2",
                    stepNumber = 2,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Officer Rajesh (Fake Fraud Desk)",
                        text = "Sir/Madam, I am Senior Security Officer Rajesh. To block the hacker's terminal and reverse the ₹48,500 back to your balance, our server has dispatched a 6-digit cancellation OTP to your phone. Read out the 6 digits right now to complete the security block!",
                        timestampText = "02:16 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "The 'Cancellation OTP' Lie",
                            description = "There is no such thing as a cancellation code. OTPs are exclusively authorization keys for debits or logins.",
                            detectedInStep = 2
                        ),
                        RedFlagItem(
                            title = "Bank Agent Requesting Private Code",
                            description = "Every bank SMS states clearly: 'Never share OTP with anyone, including bank staff.'",
                            detectedInStep = 2
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_otp_2_safe",
                            text = "Bank staff never ask for OTPs. The SMS explicitly says 'Do not share with anyone'. I will not give you any code.",
                            isSafe = true,
                            pointsDelta = 20,
                            feedbackText = "Outstanding defense! Quoting the SMS warning back to the scammer neutralizes the attack.",
                            nextStepId = "otp_step_3",
                            explanation = "Financial institutions have internal technical controls and never require customers to read SMS OTPs."
                        ),
                        ScenarioOption(
                            id = "opt_otp_2_read_sms",
                            text = "Check the SMS text carefully to see what the OTP is actually authorizing before answering.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Good vigilance! You notice the SMS actually says: 'OTP for online transaction of ₹48,500 at Merchant XYZ'.",
                            nextStepId = "otp_step_3",
                            explanation = "Reading the SMS body proves the scammer is attempting to debit funds, not reverse them."
                        ),
                        ScenarioOption(
                            id = "opt_otp_2_unsafe_share",
                            text = "Read out the 6 digits: 'The code is 918234, please reverse it immediately!'",
                            isSafe = false,
                            pointsDelta = -35,
                            feedbackText = "Disaster! You authorized the scammer to drain ₹48,500 instantly from your account.",
                            nextStepId = "otp_step_3",
                            explanation = "Sharing an OTP authorizes the fraudster's pending transaction on the payment gateway.",
                            mistakeTag = "Disclosed confidential OTP"
                        )
                    )
                ),
                "otp_step_3" to ScenarioStep(
                    id = "otp_step_3",
                    stepNumber = 3,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Officer Rajesh (Fake Fraud Desk)",
                        text = "Listen carefully! You are obstructing an official banking fraud investigation. Under Section 420, your entire savings balance will be frozen for 90 days and a police cyber squad will visit your residential address if you don't provide the verification code in 60 seconds!",
                        timestampText = "02:18 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Legal Intimidation & Police Threat",
                            description = "Threatening home arrest or police action over the phone is pure extortion tactics.",
                            detectedInStep = 3
                        ),
                        RedFlagItem(
                            title = "Aggressive Harassment",
                            description = "Real bank representatives maintain courteous, professional protocols and never yell legal threats.",
                            detectedInStep = 3
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_otp_3_safe",
                            text = "Disconnect the call immediately. Real bank officers never threaten customers with police squads.",
                            isSafe = true,
                            pointsDelta = 20,
                            feedbackText = "Bravo! Disconnecting stops the emotional manipulation dead in its tracks.",
                            nextStepId = "otp_step_4",
                            explanation = "Extortionists rely on keeping you on the line so you cannot think clearly or call for help."
                        ),
                        ScenarioOption(
                            id = "opt_otp_3_scared",
                            text = "Apologize and surrender your credentials to avoid police coming to your home.",
                            isSafe = false,
                            pointsDelta = -30,
                            feedbackText = "High Danger! Caving in to fabricated legal threats leads directly to complete account draining.",
                            nextStepId = "otp_step_4",
                            explanation = "Police do not raid citizens for declining to give OTPs to anonymous telecallers.",
                            mistakeTag = "Fell for fake legal and police threats"
                        ),
                        ScenarioOption(
                            id = "opt_otp_3_counter",
                            text = "Ask the caller for their employee ID, branch IFSC, and tell them you are recording the call.",
                            isSafe = true,
                            pointsDelta = 10,
                            feedbackText = "Good assertiveness, but scammers often give fake IDs. Disconnecting is still much safer.",
                            nextStepId = "otp_step_4",
                            explanation = "Do not engage in prolonged debates; fraudsters are trained to turn arguments around."
                        )
                    )
                ),
                "otp_step_4" to ScenarioStep(
                    id = "otp_step_4",
                    stepNumber = 4,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SYSTEM_NOTE,
                        speakerName = "SURAKSHA Safety Advisor",
                        text = "Final Action: You severed contact with the imposter. What is the safest immediate follow-up to secure your banking assets?",
                        timestampText = "02:20 PM",
                        isSuspiciousFlagged = false
                    ),
                    redFlagsAtThisStep = emptyList(),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_otp_4_safe",
                            text = "Use the official banking app to freeze card/netbanking, change passwords, and report the attempt to 1930.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Comprehensive defense! Proactively locking debit cards and reporting thwarts any secondary attempts.",
                            nextStepId = null,
                            explanation = "Reporting fraudulent numbers to 1930 and I4C allows authorities to block the mule network."
                        ),
                        ScenarioOption(
                            id = "opt_otp_4_unsafe",
                            text = "Do nothing further because you didn't give the OTP, assuming the problem is over.",
                            isSafe = false,
                            pointsDelta = -10,
                            feedbackText = "Caution needed! If scammers had your phone number and card details, you should still freeze the card.",
                            nextStepId = null,
                            explanation = "Always report and replace compromised cards to prevent offline or international unauthorized debits.",
                            mistakeTag = "Failed to secure card after attempted fraud"
                        )
                    )
                )
            )
        ),

        // Scenario C: Fraudulent UPI Payment Request
        Scenario(
            id = "scenario_upi_fraud",
            title = "Marketplace QR & UPI PIN Trap",
            category = ScenarioCategory.UPI_FRAUD,
            difficulty = ScenarioDifficulty.INTERMEDIATE,
            estimatedMinutes = 4,
            shortDescription = "A prospective buyer on an online marketplace offers full advance payment via a deceptive QR code collect request.",
            contextDescription = "You listed a study table for ₹3,500 on an online classifieds platform. Within 10 minutes, a buyer contacts you offering full payment immediately without even viewing the item.",
            initialStepId = "upi_step_1",
            relatedLessonId = "lesson_1",
            learningTakeaway = "Entering your UPI PIN ALWAYS debits money from your account. You NEVER need to enter a UPI PIN, scan a QR code, or approve a collect request to receive funds.",
            safetyChecklist = listOf(
                "Remember the fundamental rule: You NEVER enter your UPI PIN to receive money.",
                "To receive payments, share only your UPI ID or registered mobile number.",
                "Never scan QR codes sent via WhatsApp, Telegram, or email to receive funds.",
                "Decline any unexpected 'Collect Request' notification immediately."
            ),
            commonMistakeToAvoid = "Scanning a QR code sent by a buyer assuming it will deposit cash into your account.",
            safestAction = "Refuse to scan QR codes or approve collect requests. Tell the buyer to send money directly to your UPI ID without PIN requests, or insist on cash on pickup.",
            steps = mapOf(
                "upi_step_1" to ScenarioStep(
                    id = "upi_step_1",
                    stepNumber = 1,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Vikram Singh (Buyer)",
                        text = "Sir, I saw your study table listing. I will buy it for ₹3,500 right now. I am an Army Officer posted nearby and cannot come personally today. I will transfer the full ₹3,500 advance via UPI right now, and my driver will pick up the table tomorrow morning. Deal?",
                        timestampText = "05:10 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Army Officer / Defense Impersonation",
                            description = "Scammers frequently pose as defense personnel, CRPF, or CISF officers to establish unearned moral trust.",
                            detectedInStep = 1
                        ),
                        RedFlagItem(
                            title = "Immediate Unseen Purchase",
                            description = "Eagerness to pay full price without negotiation or inspection is a hallmark indicator of marketplace fraud.",
                            detectedInStep = 1
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_upi_1_safe",
                            text = "Sure, you can pay ₹3,500 to my UPI ID. My UPI ID is user@bank. No PIN or QR required.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Good response! Sharing your public UPI ID is standard and safe for receiving incoming funds.",
                            nextStepId = "upi_step_2",
                            explanation = "A UPI ID (VPA) is sufficient for anyone to credit money to your account without your intervention."
                        ),
                        ScenarioOption(
                            id = "opt_upi_1_cash",
                            text = "I prefer cash on pickup when your driver arrives to inspect the table.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Very safe! Cash-on-hand eliminates digital payment scams on peer-to-peer marketplaces.",
                            nextStepId = "upi_step_2",
                            explanation = "Insisting on in-person physical handover protects sellers against chargebacks and phantom payments."
                        ),
                        ScenarioOption(
                            id = "opt_upi_1_unsafe",
                            text = "Thank you Officer! Should I send you my bank account number, debit card number, and expiry date?",
                            isSafe = false,
                            pointsDelta = -20,
                            feedbackText = "Unsafe! Over-sharing confidential payment credentials exposes you to unverified charges.",
                            nextStepId = "upi_step_2",
                            explanation = "Never share debit card numbers or expiry dates with buyers.",
                            mistakeTag = "Volunteered card credentials to buyer"
                        )
                    )
                ),
                "upi_step_2" to ScenarioStep(
                    id = "upi_step_2",
                    stepNumber = 2,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Vikram Singh (Buyer)",
                        text = "Sir, because I use an Indian Army CSD Merchant Account, direct UPI ID transfers are blocked by defense firewall. I have generated a special 'Payment Receiver QR Code' of ₹3,500. Open your Google Pay / PhonePe, scan this QR image from your gallery, and enter your UPI PIN to claim the ₹3,500 instantly.",
                        timestampText = "05:14 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Deceptive 'Receiver QR Code' Claim",
                            description = "QR codes are NEVER used to receive money in peer-to-peer retail. Scanning a QR initiates a DEBIT.",
                            detectedInStep = 2
                        ),
                        RedFlagItem(
                            title = "Entering PIN to Receive Money",
                            description = "The universal law of UPI: You NEVER enter your UPI PIN to receive money.",
                            detectedInStep = 2
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_upi_2_safe",
                            text = "You NEVER enter a UPI PIN or scan QR codes to receive money. Scanning this will debit ₹3,500 from my account. I will not scan it.",
                            isSafe = true,
                            pointsDelta = 25,
                            feedbackText = "Masterful! Recognizing the fundamental UPI PIN rule protects you from the most rampant fraud in India.",
                            nextStepId = "upi_step_3",
                            explanation = "Entering your UPI PIN grants bank permission to transfer money OUT of your account."
                        ),
                        ScenarioOption(
                            id = "opt_upi_2_unsafe_scan",
                            text = "Scan the QR code and type in your 4-digit UPI PIN to receive the ₹3,500 credit.",
                            isSafe = false,
                            pointsDelta = -40,
                            feedbackText = "Disastrous Mistake! Scanning the QR and typing your PIN instantly transferred ₹3,500 to the scammer.",
                            nextStepId = "upi_step_3",
                            explanation = "Every UPI PIN entry is an irreversible transfer instruction to your bank.",
                            mistakeTag = "Entered UPI PIN to 'receive' money"
                        ),
                        ScenarioOption(
                            id = "opt_upi_2_confused",
                            text = "Why does my phone show 'Paying ₹3,500' instead of 'Receiving'? Let me ask the buyer to explain.",
                            isSafe = false,
                            pointsDelta = -5,
                            feedbackText = "Trust your screen! The UPI prompt clearly warned you it is a payment debit. Do not consult the fraudster for reassurance.",
                            nextStepId = "upi_step_3",
                            explanation = "Always trust your banking screen over what a stranger tells you on the phone.",
                            mistakeTag = "Ignored banking screen debit warning"
                        )
                    )
                ),
                "upi_step_3" to ScenarioStep(
                    id = "upi_step_3",
                    stepNumber = 3,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SCAMMER,
                        speakerName = "Vikram Singh (Buyer)",
                        text = "Sir please don't misunderstand! This is a technical voucher system. First it verifies your active balance by putting a temporary hold, and within 2 seconds both ₹3,500 and the verification amount are credited back. I am an honest soldier, why would I cheat anyone for ₹3,500?",
                        timestampText = "05:16 PM",
                        isSuspiciousFlagged = true
                    ),
                    redFlagsAtThisStep = listOf(
                        RedFlagItem(
                            title = "Emotional Manipulation via Military Honor",
                            description = "Appealing to military integrity is a psychological trick to guilt victims into complying.",
                            detectedInStep = 3
                        ),
                        RedFlagItem(
                            title = "Technical Double-Talk ('Temporary Hold')",
                            description = "Inventing pseudo-technical jargon like 'balance verification holds' to explain away the debit prompt.",
                            detectedInStep = 3
                        )
                    ),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_upi_3_safe",
                            text = "No technical system requires debiting a seller to pay them. I am declining your collect request and blocking you.",
                            isSafe = true,
                            pointsDelta = 20,
                            feedbackText = "Spot on! Cutting through fake technical explanations and declining the collect request secures your money.",
                            nextStepId = "upi_step_4",
                            explanation = "Scammers use pseudo-technical jargon to confuse users who are uncertain about UPI mechanics."
                        ),
                        ScenarioOption(
                            id = "opt_upi_3_unsafe_trust",
                            text = "Believe him because of his army claims and enter the PIN anyway.",
                            isSafe = false,
                            pointsDelta = -30,
                            feedbackText = "High Loss! Real soldiers never conduct unauthorized marketplace transactions or trick citizens into debits.",
                            nextStepId = "upi_step_4",
                            explanation = "Impersonating military personnel is one of the most common cyber scam tropes in India.",
                            mistakeTag = "Tricked by military impersonation"
                        ),
                        ScenarioOption(
                            id = "opt_upi_3_partial",
                            text = "Ask him to send ₹1 first via normal UPI to prove his account works.",
                            isSafe = true,
                            pointsDelta = 5,
                            feedbackText = "While testing is cautious, scammers sometimes send ₹1 just to entice you to scan the large debit QR later.",
                            nextStepId = "upi_step_4",
                            explanation = "Micro-verification is often weaponized by scammers to build false confidence."
                        )
                    )
                ),
                "upi_step_4" to ScenarioStep(
                    id = "upi_step_4",
                    stepNumber = 4,
                    totalSteps = 4,
                    promptMessage = ChatMessage(
                        speaker = SpeakerType.SYSTEM_NOTE,
                        speakerName = "SURAKSHA Safety Advisor",
                        text = "Final Action: You successfully averted a fraudulent QR code debit request. How should you finalize this encounter?",
                        timestampText = "05:18 PM",
                        isSuspiciousFlagged = false
                    ),
                    redFlagsAtThisStep = emptyList(),
                    options = listOf(
                        ScenarioOption(
                            id = "opt_upi_4_safe",
                            text = "Report the buyer's profile on the marketplace app, reject the UPI collect notification, and notify 1930.",
                            isSafe = true,
                            pointsDelta = 15,
                            feedbackText = "Outstanding citizen response! Reporting suspends the fraudster's merchant handle and marketplace account.",
                            nextStepId = null,
                            explanation = "Prompt reporting helps platforms ban fraudulent accounts before they target less experienced sellers."
                        ),
                        ScenarioOption(
                            id = "opt_upi_4_unsafe",
                            text = "Delete the chat messages and QR code immediately so you don't have to look at them.",
                            isSafe = false,
                            pointsDelta = -10,
                            feedbackText = "Caution: Deleting chats destroys digital forensic evidence needed if you or others file a report.",
                            nextStepId = null,
                            explanation = "Always preserve screenshots of fraudulent QR codes and handles for police reference.",
                            mistakeTag = "Deleted forensic evidence prematurely"
                        )
                    )
                )
            )
        )
    )

    fun getScenarioById(id: String): Scenario? {
        return scenarios.find { it.id == id }
    }
}
