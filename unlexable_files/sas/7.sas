Extract a word from a sentence and return the shorter list call transtrn

Problem remove the word 'cat' from the sentence below.

Replaces or removes all occurrences of a substring in a character string

I was unaware of transtrn (thans data_null_)

SAS Forum
https://tinyurl.com/yczpg4tj
https://communities.sas.com/t5/SAS-Programming/Limitation-of-tranwrd-function/m-p/514844

Data_null_ profile
https://communities.sas.com/t5/user/viewprofilepage/user-id/15410


INPUT
=====
                                                   | Rules
  The quick brown fox jumps over the lazy cat dog  | Remove 'cat'
                                          ---

EXAMPLE OUTPUT
--------------

  The quick brown fox jumps over the lazy dog


PROCESS
=======

data want;

   fro='The quick brown fox jumps over the lazy cat dog';
   too=compbl(transtrn(fro, "cat", ""));
   put too=;

run;quit;

TOO=The quick brown fox jumps over the lazy dog


