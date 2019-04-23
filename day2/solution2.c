#include <stdio.h>
#include <stdlib.h>

void move(int board[5][5], char, int *, int *);

int main(int argc, char *argv[]) {
  FILE *fp;
  char c;
  int x = 2;
  int y = 0;

  

  int board[5][5] = {{0, 0, 1, 0, 0}, 
                     {0, 2, 3, 4, 0},
                     {5, 6, 7, 8, 9},
                     {0, 10, 11, 12, 0},
                     {0, 0, 13, 0, 0}};

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
      printf("%x", board[x][y]); 
    else 
      move(board, c, &x, &y);
  }
}

void move(int board[5][5], char direction, int *x, int *y) {
  switch(direction) {
    case 'U':
      if (*x > 0 && board[*x-1][*y] != 0) (*x)--;
      break;
    case 'D':
      if (*x < 4 && board[*x+1][*y] != 0) (*x)++;
      break;
    case 'L':
      if (*y > 0 && board[*x][*y-1] != 0) (*y)--;
      break;
    case 'R':
      if (*y < 4 && board[*x][*y+1] != 0) (*y)++;
    default:
      break;
  }
}