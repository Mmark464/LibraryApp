USE library;

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (name)
VALUES ('ROLE_LIBRARIAN');
INSERT INTO roles (name)
VALUES ('ROLE_MEMBER');

INSERT INTO users (first_name, last_name, username, password, created_at, updated_at)
VALUES ('adminFirstName', 'adminLastName', 'adminUsername',
        '$2a$10$G3QYH6MkArUte4.XdEeLpOXF4E9NhOYk1GhZpQfSBpTqKyXMvf/6G', NOW(), NOW()),

       ('librarianFirstName', 'librarianLastName', 'librarianUsername',
        '$2a$10$PYWmsTEpFvf/KHa7SlBwquIBD0bKqdJa96/KApCQ.WsyauATbEgjO', NOW(), NOW()),

       ('memberFirstName', 'memberLastName', 'memberUsername',
        '$2a$10$7QXe9R48UTqhCvG4ushKM.8WQV6alz/q1kZKURuB1YKFHWvJWQ1NW', NOW(), NOW()),

       ('user4FirstName', 'user4LastName', 'user4Username',
        '$2a$10$brslfl.5IBrhLbFI6.dQBOB5o/oUMRe9ERlTb/f20ZFEvAsxw8it.', NOW(), NOW()),

       ('user5FirstName', 'user5LastName', 'user5Username',
        '$2a$10$Q1rqoOI1it3x8OTACRn6aeOtuLqw1WEJmIKjNPvMLIQZrsQK9REg2', NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO authors (first_name, last_name, bio, created_at, updated_at)
VALUES ('authorFirstName1', 'authorLastName1', 'authorBio1', NOW(), NOW()),
       ('authorFirstName2', 'authorLastName2', 'authorBio2', NOW(), NOW()),
       ('authorFirstName3', 'authorLastName3', 'authorBio3', NOW(), NOW()),
       ('authorFirstName4', 'authorLastName4', 'authorBio4', NOW(), NOW()),
       ('authorFirstName5', 'authorLastName5', 'authorBio5', NOW(), NOW());

INSERT INTO genres (name, description, created_at, updated_at)
VALUES ('Science Fiction', 'Futuristic and scientific stories', NOW(), NOW()),
       ('Fantasy', 'Magic and mythical worlds', NOW(), NOW()),
       ('Mystery', 'Crime and detective stories', NOW(), NOW()),
       ('Horror', 'Scary and suspenseful tales', NOW(), NOW()),
       ('Romance', 'Love and relationships', NOW(), NOW());

INSERT INTO publishing_houses (name, address, contact_number, created_at, updated_at)
VALUES ('publishingHouseName1', 'publishingHouseAddress1', 'publishingHouseContactNumber1', NOW(), NOW()),
       ('publishingHouseName2', 'publishingHouseAddress2', 'publishingHouseContactNumber2', NOW(), NOW()),
       ('publishingHouseName3', 'publishingHouseAddress3', 'publishingHouseContactNumber3', NOW(), NOW()),
       ('publishingHouseName4', 'publishingHouseAddress4', 'publishingHouseContactNumber4', NOW(), NOW()),
       ('publishingHouseName5', 'publishingHouseAddress5', 'publishingHouseContactNumber5', NOW(), NOW());

INSERT INTO books (title, price, publication_date, average_rating, publishing_house_id, created_at, updated_at)
VALUES ('bookTitle1', 10, NOW(), 1, 1, NOW(), NOW()),
       ('bookTitle2', 20, NOW(), 2, 2, NOW(), NOW()),
       ('bookTitle3', 30, NOW(), 3, 3, NOW(), NOW()),
       ('bookTitle4', 40, NOW(), 4, 4, NOW(), NOW()),
       ('bookTitle5', 50, NOW(), 5, 5, NOW(), NOW());

INSERT INTO reviews (rating, comment, review_date, book_id, created_at, updated_at)
VALUES
    (1, 'reviewComment1', NOW(), 1, NOW(), NOW()),
    (2, 'reviewComment2', NOW(), 2, NOW(), NOW()),
    (3, 'reviewComment3', NOW(), 3, NOW(), NOW()),
    (4, 'reviewComment4', NOW(), 4, NOW(), NOW()),
    (5, 'reviewComment5', NOW(), 5, NOW(), NOW());
