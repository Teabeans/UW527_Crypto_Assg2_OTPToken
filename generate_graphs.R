# R script for generating graphs used in CSS527 Assignment 2

# Make sure necessary packages are installed and loaded
install.packages("ggplot2")
library (ggplot2)

# Set numeric formatting options
options(scipen=9)

# Load data for CR1 collision plot, one million iterations
cr1_1_million.data <- read.csv(file="stats_to_one_million_iterations.txt")

# Make graph
p1 <- ggplot() + geom_line(aes(y = Collisions, x = Iteration), data = cr1_1_million.data)
p1 + labs(title = "CR1 Collision Rate by Iteration", subtitle = "To One Million Iterations", x = "Iterations", y = "Collisions") + theme(plot.title = element_text(hjust = 0.5)) + theme(plot.subtitle = element_text(hjust = 0.5))

# Export the CR1 graph to an image
ggsave("cr1_to_one_million.png")

# Load data for CR1 collision plot, ten million iterations
cr1_10_million.data <- read.csv(file="stats_to_ten_million_iterations.txt")

# Make graph
p2 <- ggplot() + geom_line(aes(y = Collisions, x = Iteration), data = cr1_10_million.data)
p2 + labs(title = "CR1 Collision Rate by Iteration", subtitle = "To Ten Million Iterations", x = "Iterations", y = "Collisions") + theme(plot.title = element_text(hjust = 0.5)) + theme(plot.subtitle = element_text(hjust = 0.5))

# Export the CR1 graph to an image
ggsave("cr1_to_ten_million.png")

