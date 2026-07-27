package com.app.hatli.core.ui.base

/**
 * Immutable UI state — single source of truth for a screen.
 * Implement as a data class; never mutate in place.
 */
interface UiState

/**
 * User intents / one-shot inputs from the UI (clicks, text changes, lifecycle).
 */
interface UiEvent

/**
 * One-time side effects consumed by the UI (navigation, snackbar, dialog).
 * Delivered via SharedFlow so they are not replayed on config change incorrectly.
 */
interface UiEffect
