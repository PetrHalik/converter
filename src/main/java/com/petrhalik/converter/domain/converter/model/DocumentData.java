package com.petrhalik.converter.domain.converter.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Internal plain document representation
 */
@Getter
@RequiredArgsConstructor
public class DocumentData {

    private final Set<String> headersSet;
    private final List<Map<String, String>> rows;
}
