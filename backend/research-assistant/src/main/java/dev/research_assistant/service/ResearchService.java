package dev.research_assistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.research_assistant.request.ResearchRequest;
import dev.research_assistant.response.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest request) {
        // Build the promot
        String prompt = buildPrompt(request);

        // Query the AI Model API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // Parse the response
        // Return response

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {

        try{
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()){
               GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);

               if(firstCandidate.getContent() != null &&
                     firstCandidate.getContent().getParts() != null &&
                     !firstCandidate.getContent().getParts().isEmpty()){
                   return firstCandidate.getContent().getParts().get(0).getText();
               }
            }
            return "No content found in the response ! ";
        } catch (Exception e){
            return "Error Parsing : "+e.getMessage();
        }
    }

    private String buildPrompt(ResearchRequest request){
        StringBuilder prompt = new StringBuilder();
        switch (request.getOperation()){
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text in few sentences:\n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. Format the response with clear heading and bullet points.\n\n");
                break;
            case "translateIntoHindi":
                prompt.append("Translate the following English text into **modern, fluent, and natural Hindi** — the kind spoken by educated native speakers today.\n\n");
                break;
            case "generateFlashcards":
                prompt.append("Based on the following text, create a list of flashcards in **Question-Answer format**. Each flashcard should contain:\n" +
                        "1. A clear and short question\n" +
                        "2. A concise and accurate answer\n" +
                        "3. Avoid copying full sentences; convert them into Q&A\n\n");
                break;
            case "referencesGenerator":
                prompt.append("Based on the following selected text, generate a list of **useful and free references** to help the user explore the topic further.\n" +
                        "\n" +
                        "Include:\n" +
                        "- YouTube videos (tutorial/explainers)\n" +
                        "- Free websites or blog articles\n" +
                        "- Freely available books or PDFs\n" +
                        "- Wikipedia or research summaries\n" +
                        "\n" +
                        "Return the references in **Markdown format** so the links are **clickable**.\n" +
                        "\n" +
                        "Each reference must include:\n" +
                        "- A short title or description\n" +
                        "- A clickable link (Markdown format)\n" +
                        "- The type of resource (in brackets, like [Video], [Article], [Book], etc.)\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown Operation: " + request.getOperation());
        }

        prompt.append(request.getContent());
        return prompt.toString();
    }
}
