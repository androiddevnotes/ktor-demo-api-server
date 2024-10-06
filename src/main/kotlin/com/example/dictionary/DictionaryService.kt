package com.example.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponse(
  val entries: List<DictionaryEntry>,
  val page: Int,
  val pageSize: Int,
  val totalEntries: Long,
  val totalPages: Int,
)

class DictionaryService(
  private val repository: DictionaryRepository,
) {
  fun createEntry(entryDTO: DictionaryEntryDTO): DictionaryEntry {
    val entry =
      DictionaryEntry(
        name = entryDTO.name,
        definition = entryDTO.definition,
        examples = entryDTO.examples,
        relatedTerms = entryDTO.relatedTerms,
        tags = entryDTO.tags,
        category = entryDTO.category,
        languages = entryDTO.languages,
        resources = entryDTO.resources,
      )
    return repository.create(entry)
  }

  fun getAllEntries(): List<DictionaryEntry> = repository.getAll()

  fun getEntryById(id: Int): DictionaryEntry? = repository.getById(id)

  fun updateEntry(
    id: Int,
    entryDTO: DictionaryEntryDTO,
  ): Boolean {
    val entry =
      DictionaryEntry(
        id = id,
        name = entryDTO.name,
        definition = entryDTO.definition,
        examples = entryDTO.examples,
        relatedTerms = entryDTO.relatedTerms,
        tags = entryDTO.tags,
        category = entryDTO.category,
        languages = entryDTO.languages,
        resources = entryDTO.resources,
      )
    return repository.update(id, entry)
  }

  fun deleteEntry(id: Int): Boolean = repository.delete(id)

  fun searchEntries(query: String): List<DictionaryEntry> = repository.search(query)

  fun getAllEntries(
    page: Int,
    pageSize: Int,
  ): DictionaryResponse {
    val entries = repository.getAll(page, pageSize)
    val totalEntries = repository.count()
    val totalPages = (totalEntries + pageSize - 1) / pageSize
    return DictionaryResponse(
      entries = entries,
      page = page,
      pageSize = pageSize,
      totalEntries = totalEntries,
      totalPages = totalPages.toInt(),
    )
  }
}
