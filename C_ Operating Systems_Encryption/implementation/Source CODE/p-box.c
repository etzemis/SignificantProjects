#include<stdio.h>
#include"files.h"

void P4(char *a)
{
	int i;
      char temp[4];
      temp[0] = a[1];
      temp[1] = a[0];
      temp[2] = a[3];
      temp[3] = a[2];
      for(i = 0; i<4 ; i++)
		a[i] = temp[i];
      return ;
}

void P8(char *a)
{
	int i;
      char temp[8];
      temp[0] = a[0];
      temp[1] = a[1];
      temp[2] = a[4];
      temp[3] = a[5];
      temp[4] = a[2];
      temp[5] = a[3];
      temp[6] = a[6];
      temp[7] = a[7];
      for(i = 0; i<8 ; i++)
		a[i] = temp[i];
      return ;
}

void P64(char *a)
{
	int i;
      char temp[64];
       temp[0] = a[0];
       temp[1] = a[12];
       temp[2] = a[23];
       temp[3] = a[25];
       temp[4] = a[38];
       temp[5] = a[42];
       temp[6] = a[53];
       temp[7] = a[59];
       temp[8] = a[22];
       temp[9] = a[9];
      temp[10] = a[26];
      temp[11] = a[32];
      temp[12] = a[1];
      temp[13] = a[47];
      temp[14] = a[51];
      temp[15] = a[61];

      temp[16] = a[24];
      temp[17] = a[37];
      temp[18] = a[18];
      temp[19] = a[41];
      temp[20] = a[55];
      temp[21] = a[58];
      temp[22] = a[8];
      temp[23] = a[2];
      temp[24] = a[16];
      temp[25] = a[3];
      temp[26] = a[10];
      temp[27] = a[27];
      temp[28] = a[33];
      temp[29] = a[46];
      temp[30] = a[48];
      temp[31] = a[62];

      temp[32] = a[11];
      temp[33] = a[28];
      temp[34] = a[60];
      temp[35] = a[49];
      temp[36] = a[36];
      temp[37] = a[17];
      temp[38] = a[4];
      temp[39] = a[43];
      temp[40] = a[50];
      temp[41] = a[19];
      temp[42] = a[5];
      temp[43] = a[39];
      temp[44] = a[56];
      temp[45] = a[45];
      temp[46] = a[29];
      temp[47] = a[13];

      temp[48] = a[30];
      temp[49] = a[35];
      temp[50] = a[40];
      temp[51] = a[14];
      temp[52] = a[57];
      temp[53] = a[6];
      temp[54] = a[54];
      temp[55] = a[20];
      temp[56] = a[44];
      temp[57] = a[52];
      temp[58] = a[21];
      temp[59] = a[7];
      temp[60] = a[34];
      temp[61] = a[15];
      temp[62] = a[31];
      temp[63] = a[63];
      for(i = 0; i<64 ; i++)
		a[i] = temp[i];
      return ;
}

void P128(char *a)
{
	int i;
      char temp[128];
      temp[0] = a[76];
      temp[1] = a[110];
      temp[2] = a[83];
      temp[3] = a[127];
      temp[4] = a[67];
      temp[5] = a[114];
      temp[6] = a[92];
      temp[7] = a[97];
      temp[8] = a[98];
      temp[9] = a[65];
      temp[10] = a[121];
      temp[11] = a[106];
      temp[12] = a[78];
      temp[13] = a[112];
      temp[14] = a[91];
      temp[15] = a[82];

      temp[16] = a[71];
      temp[17] = a[101];
      temp[18] = a[89];
      temp[19] = a[126];
      temp[20] = a[72];
      temp[21] = a[107];
      temp[22] = a[81];
      temp[23] = a[118];
      temp[24] = a[90];
      temp[25] = a[124];
      temp[26] = a[73];
      temp[27] = a[88];
      temp[28] = a[64];
      temp[29] = a[104];
      temp[30] = a[100];
      temp[31] = a[85];

      temp[32] = a[109];
      temp[33] = a[87];
      temp[34] = a[75];
      temp[35] = a[113];
      temp[36] = a[120];
      temp[37] = a[66];
      temp[38] = a[103];
      temp[39] = a[115];
      temp[40] = a[122];
      temp[41] = a[108];
      temp[42] = a[95];
      temp[43] = a[69];
      temp[44] = a[74];
      temp[45] = a[116];
      temp[46] = a[80];
      temp[47] = a[102];

      temp[48] = a[84];
      temp[49] = a[96];
      temp[50] = a[125];
      temp[51] = a[68];
      temp[52] = a[93];
      temp[53] = a[105];
      temp[54] = a[119];
      temp[55] = a[79];
      temp[56] = a[123];
      temp[57] = a[86];
      temp[58] = a[70];
      temp[59] = a[117];
      temp[60] = a[111];
      temp[61] = a[77];
      temp[62] = a[99];
      temp[63] = a[94];
      
      temp[64] = a[28];
      temp[65] = a[9];
      temp[66] = a[37];
      temp[67] = a[4];
      temp[68] = a[51];
      temp[69] = a[43];
      temp[70] = a[58];
      temp[71] = a[16];
      temp[72] = a[20];
      temp[73] = a[26];
      temp[74] = a[44];
      temp[75] = a[34];
      temp[76] = a[0];
      temp[77] = a[61];
      temp[78] = a[12];
      temp[79] = a[55];

      temp[80] = a[46];
      temp[81] = a[22];
      temp[82] = a[15];
      temp[83] = a[2];
      temp[84] = a[48];
      temp[85] = a[31];
      temp[86] = a[57];
      temp[87] = a[33];
      temp[88] = a[27];
      temp[89] = a[18];
      temp[90] = a[24];
      temp[91] = a[14];
      temp[92] = a[6];
      temp[93] = a[52];
      temp[94] = a[63];
      temp[95] = a[42];

      temp[96] = a[49];
      temp[97] = a[7];
      temp[98] = a[8];
      temp[99] = a[62];
      temp[100] = a[30];
      temp[101] = a[17];
      temp[102] = a[47];
      temp[103] = a[38];
      temp[104] = a[29];
      temp[105] = a[53];
      temp[106] = a[11];
      temp[107] = a[21];
      temp[108] = a[41];
      temp[109] = a[32];
      temp[110] = a[1];
      temp[111] = a[60];
      
      temp[112] = a[13];
      temp[113] = a[35];
      temp[114] = a[5];
      temp[115] = a[39];
      temp[116] = a[45];
      temp[117] = a[59];
      temp[118] = a[23];
      temp[119] = a[54];
      temp[120] = a[36];
      temp[121] = a[10];
      temp[122] = a[40];
      temp[123] = a[56];
      temp[124] = a[25];
      temp[125] = a[50];
      temp[126] = a[19];
      temp[127] = a[3];
      for(i = 0; i<128 ; i++)
		a[i] = temp[i];
      return ;
}

