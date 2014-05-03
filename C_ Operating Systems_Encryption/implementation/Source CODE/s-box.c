#include<stdio.h>
#include"files.h"


char S0(char a)
{
  char temp;
  switch(a)
  {
     case '0': temp = 'd'; break;
     case '1': temp = '7'; break;
     case '2': temp = '3'; break;
     case '3': temp = '2'; break;
     case '4': temp = '9'; break;
     case '5': temp = 'a'; break;
     case '6': temp = 'c'; break;
     case '7': temp = '1'; break;
     case '8': temp = 'f'; break;
     case '9': temp = '4'; break;
     case 'a': temp = '5'; break;
     case 'b': temp = 'e'; break;
     case 'c': temp = '6'; break;
     case 'd': temp = '0'; break;
     case 'e': temp = 'b'; break;
     case 'f': temp = '8'; break;
  }
  return temp;
}

char D(char a)
{
  char temp;
  switch(a)
  {
     case '0': temp = '0'; break;
     case '1': temp = 'e'; break;
     case '2': temp = 'd'; break;
     case '3': temp = '3'; break;
     case '4': temp = 'b'; break;
     case '5': temp = '5'; break;
     case '6': temp = '6'; break;
     case '7': temp = '8'; break;
     case '8': temp = '7'; break;
     case '9': temp = '9'; break;
     case 'a': temp = 'a'; break;
     case 'b': temp = '4'; break;
     case 'c': temp = 'c'; break;
     case 'd': temp = '2'; break;
     case 'e': temp = '1'; break;
     case 'f': temp = 'f'; break;
  }
  return temp;
}

char S1(char a)
{
  char temp;
  switch(a)
  {
     case '0': temp = '4'; break;
     case '1': temp = 'a'; break;
     case '2': temp = 'f'; break;
     case '3': temp = 'c'; break;
     case '4': temp = '0'; break;
     case '5': temp = 'd'; break;
     case '6': temp = '9'; break;
     case '7': temp = 'b'; break;
     case '8': temp = 'e'; break;
     case '9': temp = '6'; break;
     case 'a': temp = '1'; break;
     case 'b': temp = '7'; break;
     case 'c': temp = '3'; break;
     case 'd': temp = '5'; break;
     case 'e': temp = '8'; break;
     case 'f': temp = '2'; break;
  }
  return temp;
}
