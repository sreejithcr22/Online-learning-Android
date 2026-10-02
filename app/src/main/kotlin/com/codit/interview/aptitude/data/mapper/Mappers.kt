package com.codit.interview.aptitude.data.mapper

import com.codit.interview.aptitude.core.util.SqliteText
import com.codit.interview.aptitude.data.local.QuestionRow
import com.codit.interview.aptitude.data.local.TipRow
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.Tip

/**
 * Normalises the loosely-typed SQLite columns into domain models.
 *
 * This is the only place that knows the database stores options as "option2" and
 * missing values as the literal string "null".
 */
fun QuestionRow.toDomain(): Question = Question(
    number = number,
    text = text,
    options = options,
    correctOptionIndex = SqliteText.optionIndex(correctOptionColumn) ?: 0,
    explanation = explanation?.takeUnless { SqliteText.isNull(it) }?.trim()?.takeIf { it.isNotEmpty() },
    answeredOptionIndex = SqliteText.optionIndex(attemptedOptionColumn),
    note = note.takeUnless { SqliteText.isNull(it) }.orEmpty(),
    timeTakenSeconds = SqliteText.durationSeconds(timeText),
    isFavourite = favourite,
)

fun TipRow.toDomain(): Tip = Tip(
    number = number,
    title = title.takeUnless { SqliteText.isNull(it) }.orEmpty(),
    body = body,
    isFavourite = favourite,
)
