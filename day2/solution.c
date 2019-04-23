#include <stdio.h>
#include <stdlib.h>

void move(char, int *, int *);

int main(int argc, char *argv[]) {
  FILE *fp;
  char c;
  int x, y;
  x = y = 1;

  int board[3][3] = {{1, 2, 3}, 
                     {4, 5, 6},
                     {7, 8, 9}};

  if (argc != 2) {
    fprintf(stderr, "No input file specified");
    exit(1);
  }

  if (!(fp = fopen(argv[1], "r"))) {
    perror(argv[1]);
    exit(1);
  }

  while ((c = fgetc(fp)) != EOF) {
    if (c == '\n') 
      printf("%d", board[x][y]); 
    else 
      move(c, &x, & y);
  }
}

void move(char direction, int *x, int *y) {
  switch(direction) {
    case 'U':
      if (*x > 0) (*x)--;
      break;
    case 'D':
      if (*x < 2) (*x)++;
      break;
    case 'L':
      if (*y > 0) (*y)--;
      break;
    case 'R':
      if (*y < 2) (*y)++;
    default:
      break;
  }
}