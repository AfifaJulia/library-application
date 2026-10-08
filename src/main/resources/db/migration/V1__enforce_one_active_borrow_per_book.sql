CREATE UNIQUE INDEX uk_active_book_borrow
ON book_borrows (book_id)
WHERE returned_at IS NULL;
