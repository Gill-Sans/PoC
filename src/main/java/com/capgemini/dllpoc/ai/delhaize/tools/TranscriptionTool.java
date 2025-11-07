package com.capgemini.dllpoc.ai.delhaize.tools;

import org.springframework.ai.tool.annotation.Tool;

public class TranscriptionTool {

    @Tool(
            description = "Transcribes audio from a Twilio recording URL and converts the text to the selected language. Currently uses a mock function for transcription and translation."
    )
    public String transcribeAudioFromUrl(String audioUrl, String language) {
        return mockTranscription(audioUrl, language);
    }

    private String mockTranscription(String audioUrl, String language) {
        return "This is a simulated transcription of the audio file.";
    }
}
