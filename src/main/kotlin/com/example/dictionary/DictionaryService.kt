package com.example.dictionary

class DictionaryService(private val repository: DictionaryRepository) {
    fun createEntry(entryDTO: DictionaryEntryDTO): DictionaryEntry {
        val entry = DictionaryEntry(
            name = entryDTO.name,
            definition = entryDTO.definition,
            examples = entryDTO.examples,
            relatedTerms = entryDTO.relatedTerms,
            tags = entryDTO.tags,
            category = entryDTO.category,
            languages = entryDTO.languages,
            resources = entryDTO.resources
        )
        return repository.create(entry)
    }

    fun getAllEntries(): List<DictionaryEntry> = repository.getAll()

    fun getEntryById(id: Int): DictionaryEntry? = repository.getById(id)

    fun updateEntry(id: Int, entryDTO: DictionaryEntryDTO): Boolean {
        val entry = DictionaryEntry(
            id = id,
            name = entryDTO.name,
            definition = entryDTO.definition,
            examples = entryDTO.examples,
            relatedTerms = entryDTO.relatedTerms,
            tags = entryDTO.tags,
            category = entryDTO.category,
            languages = entryDTO.languages,
            resources = entryDTO.resources
        )
        return repository.update(id, entry)
    }

    fun deleteEntry(id: Int): Boolean = repository.delete(id)

    fun searchEntries(query: String): List<DictionaryEntry> = repository.search(query)
}