-- Function to insert dictionary entries
CREATE OR REPLACE FUNCTION insert_dictionary_entry(
    p_name TEXT,
    p_definition TEXT,
    p_examples TEXT[],
    p_related_terms TEXT[],
    p_tags TEXT[],
    p_category TEXT,
    p_languages TEXT[],
    p_resources TEXT[]
) RETURNS VOID AS $$
BEGIN
    INSERT INTO dictionary_entries (name, definition, examples, related_terms, tags, category, languages, resources, created_at, updated_at)
    VALUES (
        p_name,
        p_definition,
        array_to_string(p_examples, '|'),
        array_to_string(p_related_terms, '|'),
        array_to_string(p_tags, '|'),
        p_category,
        array_to_string(p_languages, '|'),
        array_to_string(p_resources, '|'),
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );
END;
$$ LANGUAGE plpgsql;

-- Insert dictionary entries
SELECT insert_dictionary_entry(
    'Algorithm',
    'A step-by-step procedure or formula for solving a problem or accomplishing a task.',
    ARRAY['Sorting algorithms', 'Search algorithms'],
    ARRAY['Data structure', 'Complexity'],
    ARRAY['Computer Science', 'Programming'],
    'Computer Science',
    ARRAY['General'],
    ARRAY['https://en.wikipedia.org/wiki/Algorithm', 'https://www.khanacademy.org/computing/computer-science/algorithms']
);

SELECT insert_dictionary_entry(
    'API',
    'Application Programming Interface: A set of protocols and tools for building software applications.',
    ARRAY['REST API', 'GraphQL API'],
    ARRAY['Endpoint', 'Request', 'Response'],
    ARRAY['Web Development', 'Software Integration'],
    'Software Development',
    ARRAY['Multiple'],
    ARRAY['https://www.redhat.com/en/topics/api/what-are-application-programming-interfaces', 'https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Client-side_web_APIs/Introduction']
);

SELECT insert_dictionary_entry(
    'Machine Learning',
    'A subset of artificial intelligence that enables systems to learn and improve from experience without being explicitly programmed.',
    ARRAY['Image recognition', 'Natural language processing'],
    ARRAY['Artificial Intelligence', 'Deep Learning', 'Neural Networks'],
    ARRAY['AI', 'Data Science'],
    'Artificial Intelligence',
    ARRAY['Python', 'R', 'Java'],
    ARRAY['https://www.ibm.com/cloud/learn/machine-learning', 'https://www.coursera.org/learn/machine-learning']
);

-- Drop the function after use
DROP FUNCTION insert_dictionary_entry;