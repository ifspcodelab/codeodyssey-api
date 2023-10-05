package app.codeodyssey.codeodysseyapi.resolution.service;

import org.springframework.web.multipart.MultipartFile;

public record CreateResolutionCommand(
        MultipartFile resolutionFile) {}
