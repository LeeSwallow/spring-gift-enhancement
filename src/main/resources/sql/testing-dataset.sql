CREATE TABLE IF NOT EXISTS  roles (
                        name VARCHAR(20) NOT NULL,
                        PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS users (
                       id BIGINT AUTO_INCREMENT,
                       email VARCHAR(255) NOT NULL UNIQUE ,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_roles (
                            user_id BIGINT NOT NULL,
                            role_name VARCHAR(20) NOT NULL,
                            PRIMARY KEY (user_id, role_name),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
                          id BIGINT AUTO_INCREMENT,
                          name VARCHAR(15) NOT NULL,
                          price BIGINT NOT NULL,
                          image_url VARCHAR(255) NOT NULL,
                          owner_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (id),
                          FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wished_products (
                                 user_id BIGINT NOT NULL,
                                 product_id BIGINT NOT NULL,
                                 quantity INT NOT NULL DEFAULT 1,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (user_id, product_id),
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                 FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


INSERT INTO roles (name) VALUES
      ('ROLE_ADMIN'),
        ('ROLE_MD'),
      ('ROLE_USER');
-- password = "qwerty1234@"
INSERT INTO users (email, password) VALUES
      ('test@test.com', '1469f57c482317fba59bb34d16c10b0f5116e64c2201e430a70cc16a34a6a785');

INSERT INTO user_roles (user_id, role_name) VALUES
      (1, 'ROLE_ADMIN'); -- Admin

INSERT INTO products (name, price, image_url, owner_id) VALUES
      ('Product 01', 1000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 02', 2000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 03', 3000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 04', 4000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 05', 5000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 06', 6000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 07', 7000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 08', 8000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 09', 9000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1),
      ('Product 10', 10000, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8c25lYWtlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"', 1);

INSERT INTO wished_products (user_id, product_id, quantity) VALUES
      (1, 1, 1),
      (1, 2, 2),
      (1, 3, 3),
      (1, 4, 4),
      (1, 5, 5);
