package com.example.data

data class SampleMessage(
    val category: ScamCategory,
    val title: String,
    val text: String
)

object SampleData {
    val samples: List<SampleMessage> = listOf(
        SampleMessage(
            category = ScamCategory.UPI_FRAUD,
            title = "UPI / Payment Fraud",
            text = "Dear customer, your UPI account has been suspended. Send Rs.1 to verify: upi://pay?pa=verify-upi@fakebank to reactivate within 2 hours or your account will be permanently blocked. — FakeBank Team"
        ),
        SampleMessage(
            category = ScamCategory.FAKE_JOB,
            title = "Fake Job / Recruitment",
            text = "Congratulations! You have been selected for a Work From Home data entry job. Salary Rs.35,000/month. Pay Rs.1,500 registration fee to confirm your slot. Contact HR Priya: 9XXXXXXXXX"
        ),
        SampleMessage(
            category = ScamCategory.LOAN_OFFER,
            title = "Loan / Financial Offer",
            text = "Pre-approved personal loan of Rs.5,00,000 at 0% interest. No documents needed. Pay Rs.2,000 processing fee now to release your loan. Offer expires in 24 hours!"
        ),
        SampleMessage(
            category = ScamCategory.PHISHING,
            title = "Phishing / Suspicious Website",
            text = "Your KYC is pending. Update now at http://sbi-kyc-update.xyz/verify or your account will be blocked. Enter your net banking ID and password to complete."
        ),
        SampleMessage(
            category = ScamCategory.FAKE_CUSTOMER_CARE,
            title = "Fake Customer Care",
            text = "This is Amazon customer care. Your order has a refund of Rs.2,340. To process the refund, please share your UPI PIN and OTP that we will send to your number."
        ),
        SampleMessage(
            category = ScamCategory.OTP_SECURITY,
            title = "OTP / Account Security Scam",
            text = "ALERT: Someone tried to access your account from a new device. Call our security team immediately at 9XXXXXXXXX and share the OTP you receive to protect your account."
        ),
        SampleMessage(
            category = ScamCategory.INVESTMENT_REWARD,
            title = "Investment / Reward Offer",
            text = "You have won Rs.50,000 in the Lucky Draw! To claim your prize, pay Rs.500 tax clearance fee to: prizeclaim@upi and share your Aadhaar and bank account number."
        ),
        SampleMessage(
            category = ScamCategory.OTHER,
            title = "Other / Marketplace Scam",
            text = "Hi, I found your number online. I am selling my iPhone 13 for Rs.8,000 only. Urgent need of money. Pay advance via UPI and I will ship today. No returns."
        )
    )

    val sampleUrls: List<String> = listOf(
        "http://sbi-kyc-update.xyz/verify",
        "https://paytm-reward.in/claim-prize",
        "http://192.168.4.12/bank-login",
        "https://bit.ly/instant-loan-approval",
        "http://secure-account-verification-axis.ga/otp"
    )

    val safetyTips: List<String> = listOf(
        "Banks and legitimate organizations NEVER ask for your UPI PIN or OTP to receive money or refunds. You only enter your PIN when sending money.",
        "Never install remote screen-sharing apps like AnyDesk, TeamViewer, or QuickSupport at the instruction of someone on a phone call.",
        "Electricity boards never threaten immediate disconnection via personal mobile numbers. Always check your official utility bill portal.",
        "Government schemes do not require registration charges paid into private individual UPI handles.",
        "If you suspect fraudulent money debit, call national cybercrime helpline 1930 within the 'Golden Hour' (first 2-3 hours) for highest recovery chance."
    )
}
