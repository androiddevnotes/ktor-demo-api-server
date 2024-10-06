DO $$
BEGIN
    -- Remove 'tags' column if it exists
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='quotes' AND column_name='tags') THEN
        ALTER TABLE quotes DROP COLUMN tags;
    END IF;

    -- Remove 'related_quotes' column if it exists
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='quotes' AND column_name='related_quotes') THEN
        ALTER TABLE quotes DROP COLUMN related_quotes;
    END IF;

    -- Remove 'additional_resources' column if it exists
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='quotes' AND column_name='additional_resources') THEN
        ALTER TABLE quotes DROP COLUMN additional_resources;
    END IF;
END $$;