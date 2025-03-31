#include <stdio.h>
#include <string.h>

//DO NOT RUN

void vulnerableFunction() {
    char buffer[10];
    printf("Enter some text: ");
    gets(buffer); 
    printf("You entered: %s\n", buffer);
}

int main() {
    vulnerableFunction();
    return 0;
}