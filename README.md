# 🧠 Research Assistant (Gemini API Integration)

A smart and lightweight browser-based research assistant that helps users summarize text, get topic suggestions, and generate insights using Google's Gemini API.

---

## 🚀 Features

- 🔍 Summarize selected text from web pages
- 💡 Suggest related research topics
- 🌐 Fast and secure Gemini API integration
- 🔐 API key protected (loaded via environment variables)
- 🌈 Clean, minimal UI with extension support

---

## 📁 Project Structure

Research-Assistant/
├── frontend/ # Browser extension or UI (VS Code)
├── backend/ # Spring Boot API (IntelliJ)
└── README.md


---

## ⚙️ Technologies Used

- **Backend:** Java, Spring Boot, WebClient
- **Frontend:** HTML, JS (or React if used)
- **API:** Gemini Pro (Google Generative AI)
- **Build Tools:** Maven, Webpack (if used)

---

## 🔐 How API is Protected

- API key is **not exposed** to frontend
- Stored securely in `application.properties`
- Access only via backend endpoint (`/gemini`)

---

## 🛠️ How to Run

### ▶ Backend (Spring Boot)

```bash
cd backend
# Add your API key in application.properties
# gemini.api.key=YOUR_API_KEY
./mvnw spring-boot:run


