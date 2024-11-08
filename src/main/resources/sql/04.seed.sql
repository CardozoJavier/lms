-- INSERT books
INSERT INTO book (title, category, genre, author, language)
VALUES
    ('The Great Gatsby', 'Fiction', 'Classic', 'F. Scott Fitzgerald', 'English'),
    ('To Kill a Mockingbird', 'Fiction', 'Historical', 'Harper Lee', 'English'),
    ('1984', 'Fiction', 'Dystopian', 'George Orwell', 'English'),
    ('Pride and Prejudice', 'Fiction', 'Romance', 'Jane Austen', 'English'),
    ('Moby-Dick', 'Fiction', 'Adventure', 'Herman Melville', 'English'),
    ('The Odyssey', 'Epic', 'Mythology', 'Homer', 'Ancient Greek'),
    ('War and Peace', 'Historical', 'Classic', 'Leo Tolstoy', 'Russian'),
    ('The Catcher in the Rye', 'Fiction', 'Coming-of-Age', 'J.D. Salinger', 'English'),
    ('The Hobbit', 'Fantasy', 'Adventure', 'J.R.R. Tolkien', 'English'),
    ('Brave New World', 'Fiction', 'Science Fiction', 'Aldous Huxley', 'English');

-- INSERT customers
INSERT INTO customer (first_name, last_name, phone_number, email)
VALUES
    ('Alice', 'Smith', '1234567890', 'alice.smith@example.com'),
    ('Bob', 'Johnson', '2345678901', 'bob.johnson@example.com'),
    ('Catherine', 'Brown', '3456789012', 'catherine.brown@example.com'),
    ('David', 'Wilson', '4567890123', 'david.wilson@example.com'),
    ('Eva', 'Davis', '5678901234', 'eva.davis@example.com');
