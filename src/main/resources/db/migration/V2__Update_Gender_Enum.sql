-- Drop the existing check constraint
ALTER TABLE participants DROP CONSTRAINT IF EXISTS participants_gender_check;

-- First, create a temporary type with the new values
CREATE TYPE gender_new AS ENUM ('MALE', 'FEMALE');

-- Update the column to use the new type
ALTER TABLE participants 
    ALTER COLUMN gender TYPE gender_new 
    USING CASE gender::text 
        WHEN 'Male' THEN 'MALE'::gender_new 
        WHEN 'Female' THEN 'FEMALE'::gender_new 
    END;

-- Drop the old type
DROP TYPE gender;

-- Add back the check constraint
ALTER TABLE participants ADD CONSTRAINT participants_gender_check 
    CHECK (gender IN ('MALE', 'FEMALE')); 