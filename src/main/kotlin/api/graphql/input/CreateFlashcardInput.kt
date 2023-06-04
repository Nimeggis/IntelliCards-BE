package api.graphql.input

import com.expediagroup.graphql.generator.scalars.ID

class CreateFlashcardInput(val question: String, val answer: String, val chapter: ID)