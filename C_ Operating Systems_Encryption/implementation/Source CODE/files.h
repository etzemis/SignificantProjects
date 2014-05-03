#include<stdio.h>
#include<string.h>

/////from file:      transform.c   ////////////////////
char xor(char , char );

char* asci_to_bin(unsigned char );

unsigned char bin_to_asci(char *);

void hex_to_bin(char , char* );

char bin_to_hex(char *);

/////from file:      s-box.c    //////////////////////

char S0(char );

char D(char );

char S1(char );

/////from file:      p-box.c    //////////////////////

void P4(char *);

void P8(char *);

void P64(char *);

void P128(char *);

/////from file:      round.c    //////////////////////

char* gama(char *);

char* SK(char * , char *);

char* EK(char *,char *);

/////from file:      key.c      /////////////////////

char* TC(char * , int );

char* BC(char *, int );

char* E(char * );

char* f(char *,int);

//////from file:     iceberg.c    ///////////////////

char* iceberg_encrypt(char*,char*,int);
char* iceberg_decrypt(char*,char*,int);
