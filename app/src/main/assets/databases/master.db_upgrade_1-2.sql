
UPDATE age SET option2='39 years', explanation='Given that
J+10=2(H-10) and
J=H+9

Solving we get H=39' WHERE qno=2;
UPDATE age SET explanation='10 years ago ratio of ages is x:2x:3x.
After 10 years: x+10:2(x+10):3(x+10)

Therefore equation becomes:
x+10+2(x+10)+3(x+10)=90.
6x=30 or x=5.

So, present age of John
= 2(x+10)= 2(5+10)=30.' WHERE qno=6;
UPDATE age SET option1='10 and 20', option2='10 and 40', option3='20 and 40', option4='15 and 30', explanation='Let Franco = X and Alfred = 4X

so , X-5+4X-5=4X
X=10
So Franco = 10 and Alfred = 40' WHERE qno=17;
UPDATE age SET que='A is two years older than B who is twice as old as C. If the total of the ages of A, B and C be 27, the how old is B?', answer='option1', explanation='Let C''s age be x years.
Then, B''s age = 2x years.
A''s age = (2x + 2) years.

 (2x + 2) + 2x + x = 27

 5x = 25

 x = 5.

Hence, B''s age = 2x = 10 years.' WHERE qno=22;
UPDATE age SET que='A father is 24 years older than his son. In two years, his age will be twice the age of his son. The present age of his son is?', option2='22 Years', explanation='Let the son''s present age be x years. Then, man''s present age = (x + 24) years.

 (x + 24) + 2 = 2(x + 2)

 x + 26 = 2x + 4

 x = 22' WHERE qno=25;
UPDATE age SET option1='1', option2='1.4', option3='1.2' WHERE qno=27;
UPDATE age SET que='The sum of the present ages of a father and his son is 60 years. Six years ago, father''s age was five times the age of the son. After 6 years, son''s age will be?', option1='20 years', option2='22 years', option3='25 years', option4='15 years', answer='option1', explanation='Let the present ages of son and father be
x and (60 -x) years respectively.

Then, (60 - x) - 6 = 5(x - 6)

 54 - x = 5x - 30

 6x = 84

 x = 14.

 Son''s age after 6 years = (x+ 6) = 20 years' WHERE qno=28;
UPDATE age SET explanation='Let their present ages be 4x,7x and 9x years respectively.

Then, (4x-8)+(7x-8)+(9x-8)= 56
20x = 80
x= 4.
Their present ages are 16,28,36 years ' WHERE qno=30;
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'14 years ago ram was 4 times the age of pankaj. If the present age of ram is twice the age of pankaj. what will be the total of their present ages?','28','7','63','77','option3','14 years ago
pankaj age= x
ram age age= 4x
Present age
pankaj age= x + 4
ram age age= 4x + 4
4x+4 = 2x+28
2x = 14
x = 7

sum of present age of pankat and ram = (7+4) + ((4*7)+4)
= 63',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'fathers age is equal to sum of ages of five children. in 15 years his age will be only half of sum of their ages. how old is father?','45','66','87','35','option1','Lets Father age be x
Let Sons age be A,B,C,D,E
x = A+B+C+D+E
After 15 years
x+15 = 1/2 ((A+B+C+D+E)+(15*5))
2X+30 = X+75
X = 45',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'A man said to his son "I am eight times as old as you were when i was as old as you are".Find the present ages if the sum of their ages is 75 years?','49,26','47,28','48,27','45,30','option3','when the son was 3 years old then his fathers age is 3*8=24 it satisfies the condition check it with the option 3.........now option 3 is 47 and 28......ryt 48-24=24......and 27-24 =3........if we take option 1 ......49-24=25 and 26-25=1 ....1*8=8....does not satisfy the condition. check with other options also.',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'10 year ago Chandravati''s mother was 4 times older than her daughter. After 10 years, the mother will be twice older than the daughter. the present age of chandravati is:','50','20','25','55','option1','50 is chandravati''s age and 20 is her daughter''s age.',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'Father is aged three times more than his son Ronit. After 8 years, he would be two and a half times of Ronit''s age. After further 8 years, how many times would he be of Ronit''s age?','2 times','2.5 times','3 times','1.5 times','option1','Let Ronit''s present age be x years. Then, father''s present age =(x + 3x) years = 4x years.
2(4x + 8) =5(x + 8)

 8x + 16 = 5x + 40

 3x = 24

 x = 8.

Hence, required ratio :(4x + 16)        =48
(x + 16) is24

48/24=2.
',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,'The sum of ages of 5 children born at the intervals of 3 years each is 50 years. What is the age of the youngest child?','4','8 ','10','none','option1','Let Ronit''s present age be x years.
Then, father''s present age =(x + 3x) years = 4x years.
2(4x + 8) =5(x + 8)

 8x + 16 = 5x + 40

 3x = 24

 x = 8.

Hence, required ratio :(4x + 16)        =48
(x + 16) is24

48/24=2.
',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'The total age of A and B is 12 years more than the total age of B and C. C is how many year younger than A?','10','11','12','13','option3','Given that,
 A + B =12+ B + C
=> A - C
=12
Therefore, C is younger than A by 12 years.',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'A father said to his son, "I was as old as you are at the present at the time of your birth". If the father''s age is 38 years now, the son''s age five years back was:','14','19','33','38','option1','et the son''s present age be x years. Then, (38 - x) = x

 2x = 38.

 x = 19.

 Son''s age 5 years back (19 - 5) = 14 years.',NULL,NULL,NULL,NULL);
INSERT INTO age(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(39,'A is two years older than B who is twice as old as C. If the total of the ages of A, B and C be 27, the how old is B?','7','8','9','10','option4','Let the son''s present age be x years.
Then, (38 - x) = x

2x = 38.
x = 19.

 Son''s age 5 years back (19 - 5) = 14 years.',NULL,NULL,NULL,NULL);
UPDATE analogy SET que='Crime:Police :: Flood: ?' WHERE qno=3;
UPDATE analogy SET que='Blot:Stain :: Lean: ?' WHERE qno=19;
UPDATE analogy SET answer='option3' WHERE qno=21;
UPDATE analogy SET que='Mountain:Valley :: ? : Enemy', option2='Father' WHERE qno=22;
UPDATE analogy SET que='Architect : Building :: Sculptor : ?', option1='Statue', option2='Stone', option4='Museum', explanation='As ''Architect'' makes ''Building'' similarly ''Sculptor'' makes ''Statue''' WHERE qno=25;
UPDATE avg SET option4='15 years', explanation='The age of the 4th girl is (4 X 12 ) - (8 + 12 + 13) = 48 - 33 = 15 years.' WHERE qno=5;
UPDATE avg SET answer='option3' WHERE qno=12;
UPDATE avg SET que='The captain of a cricket team of 11 members is 26 years old and the wicket keeper is 3 years older. If the ages of these two are excluded, the average age of the remaining players is one year less than the average age of the whole team. What is the average age of the team?', option1='23 years', option2='25 years', option3='15 years', option4='30 years', explanation='Let the average age of the whole team by x years.

 11x - (26 + 29) = 9(x -1)

 11x - 9x = 46

 2x = 46

 x = 23.

So, average age of the team is 23 years.' WHERE qno=17;
UPDATE avg SET answer='option2' WHERE qno=22;
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'The average price of 10 books is Rs.12 while the average price of 8 of these books is Rs.11.75. Of the remaining two books, if the price of one book is 60% more than the price of the other, what is the price of each of these two books?','5,8','8,9','10,16','3,5','option3','Total cost of 10 books = Rs. 120

Total cost of 8 books = Rs. 94

=> The cost of 2 books = Rs. 26

Let the price of each book be x and y.

=> x + y = 26 ---------------- (1)

Given that the price of 1 book is 60% more than the other price

(160y/100)+y=26

26y=260

y=10

Substituting Y=10 (1) we get,

x+10=26

x = 16


',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'In a set of three numbers, the average of first two numbers is 2, the average of the last two numbers is 3, and the average of the first and the last numbers is 4. What is the average of three numbers?','2','5','3','4','option3','Let the three numbers be x,y and z.

We are given that

x+y / 2 = 2

y+z / 2 = 3

x+z / 2 = 4

Summing the three equations yields

(x+y)/2 + (y+z)/2 + (x+z)/2=2+3+4

x+y+z=9

The average of the three numbers is

=x+y+z / 3

=9/3=3



',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'The average monthly salary of 12 workers and 3 managers in a factory was Rs. 600. When one of the manager whose salary was Rs. 720, was replaced with a new manager, then the average salary of the team went down to 580. What is the salary of the new manager?','570','420','690','640','option2','The total salary amount = 15*600=9000

The salary of the exiting manager = 720.

Therefore, the salary of 12 workers and the remaining 2 managers:

=9000-720=8280

When a new manager joins, the new average salary drops to Rs.580 for the total team of 15 of them.

The total salary for the 15 people i.e., 12 workers, 2 old managers and 1 new manager = 580*15=8700

Therefore, the salary of the new manager is 9000-8700=300 less than that of the old manager who left the company, which is equal to 720-300=420

',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'The average of five different positive numbers is 25. xx is the decrease in the average when the smallest number among them is replaced by 0. What can be said about xx?
','x is less than 5','x>5','x=5','nothing ','option1','Let a,b,c,d and e be the five positive numbers in the decreasing order of size such that e is the
smallest number. We are given that the average of the five numbers is 25.

Hence, we have the equation

a+b+c+d+e / 5=25

a+b+c+d+e=125 by multiplying by 5 ---(1)

The smallest number in a set is at least less than the average of the numbers in the set if at least
one number is different.

For example, the average of 1, 2, and 3 is 2, and the smallest number in the set 1 is less than the

average 2. Hence, we have the inequality

0<e<25
0>-e>-25 by multiplying both sides of the inequality by -1 and flipping the directions of
the inequalities.

Adding this inequality to equation (1) yields

0+125>(a+b+c+d+e)+(-e)>125-25

125>(a+b+c+d)>100

125>(a+b+c+d+0)>100 by adding by 0

25>a+b+c+d+0 / 5=>20 by dividing the inequality by 5

25> The average of numbers a,b,c,d and 0>20

Hence, x equals

(Average of the numbers a,b,c,d and e)-(Average of the numbers a,b,c and d)

=25-(A number between 20 and 25)

=> A number less than 5

Hence, x <5',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'In 2011, the arithmetic mean of the annual incomes of Ramesh and Suresh was Rs. 3800. The arithmetic mean of the annual incomes of Suresh and Pratap was Rs. 4800, and the arithmetic mean of the annual incomes of Pratap and Ramesh was Rs. 5800. What is the arithmetic mean of the incomes of the three?','4000','4200','4400','4800','option4','Let a,b and c be the annual incomes of Ramesh, Suresh, and Pratap, respectively.

Now, we are given that

The  arithmetic  mean  of  the  annual  incomes  of  Ramesh  and  Suresh  was  Rs. 3800.

Hence, a+b/ 2=3800

Multiplying by 2 yields a+b=2*3800=7600

The  arithmetic  mean  of  the  annual  incomes  of  Suresh  and  Pratap  was  Rs. 4800.

Hence, b+c/2=4800

Multiplying by 2 yields b+c=2*4800=9600

The  arithmetic  mean  of  the  annual  incomes  of  Pratap  and  Ramesh  was  Rs. 5800.

Hence, c+a/2=5800

Multiplying by 2 yields c+a=2*5800=11,600

Summing these three equations yields:

(a+b)+(b+c)+(c+a)=7600+9600+11,600

2a+2b+2c=28,800

a+b+c=14,400

The average of the incomes of the three equals the sum of the incomes divided by 3:

=a+b+c/2=a+b+c2

=14,400/3=4800',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,'In Arun''s opinion, his weight is greater than 65 kg but less than 72 kg. His brother does not agree with Arun and he thinks that Arun''s weight is greater than 60 kg but less than 70 kg. His mother''s view is that his weight cannot be greater than 68 kg. If all are them are correct in their estimation, what is the average of different probable weights of Arun?','67','68','69','data inadequate','option1','Let Arun''s weight by X kg.

According to Arun:

65<X<72

According to Arun''s brother:

60<X<70

According to Arun''s mother:

X<=68

The values satisfying all the above conditions are 66, 67 and 68.

Required average

=66+67+68=201/3=67kg
',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'A student finds the average of 10 positive integers. Each integer contains two digits. By mistake, the boy interchanges the digits of one number say ba for ab. Due to this, the average becomes 1.8 less than the previous one. What was the difference of the two digits aa and bb?','8','6','2','4','option3','Let the original number be ab i.e., (10a+b)

After interchanging the digits, the new number becomes ba i.e., (10b+a)

The question states that the average of 10 numbers has become 1.8 less than the original average.

Therefore, the sum of the original 10 numbers will be 10*1.8 more than the sum of the 10 numbers with the digits interchanged.

i.e., 10a+b=10b+a+18

9a-9b=18

a-b = 2



',NULL,NULL,NULL,NULL);
INSERT INTO avg(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'The average wages of a worker during a fortnight comprising 15 consecutive working days was Rs.90 per day. During the first 7 days, his average wages was Rs.87/day and the average wages during the last 7 days was Rs.92 /day. What was his wage on the 8th day?

','99','97','83','92','option2','The total wages earned during the 15 days that the worker worked :

=15*90=Rs.1350

The total wages earned during the first 7 days = 7*877 = Rs. 609.

The total wages earned during the last 7 days = 7*927= Rs. 644.

Total wages earned during the 15 days = wages during first 7 days + wage on 8th day + wages during the last 7 days.

1350=609+ wage on 8th day +644

wage on 8th day = 1350-609-644= Rs. 97',NULL,NULL,NULL,NULL);
UPDATE awards SET option1='Emil Jannings' WHERE qno=1;
UPDATE awards SET option3='The Fundamental Physics Prize',answer='option3' WHERE qno=3;
UPDATE basic_gk SET answer='option2' WHERE qno=23;
UPDATE blood SET option1='Uncle' WHERE qno=3;
UPDATE blood SET que='Pointing to a person, Deepak said, "His only brother is the father of my daughter''s father". How is the person related to Deepak?', option2='Uncle', explanation='Father of Deepak''s daughter''s father ΓåÆ Deepak''s father.

Hence, the person in the brother of Deepak''s father.

Therefore, the person is the uncle of Deepak.' WHERE qno=6;
UPDATE blood SET que='P is the mother of K, K is the sister of D, D is the father of J. How is P related to J?', option4='Grand mother', explanation='P is the mother of K

K is the sister of D

D is the father of J.

Therefore, J is the nephew or niece of K and P is the grandmother of J.' WHERE qno=9;
UPDATE blood SET que='If A + B means A is the brother of B , A % B means A is the father of B and A x B means A is the sister of B. Which of the following means M is the uncle of P?', option1='N x P % M', option2='M % N x P', option3='M + S % R % P', option4='M + K % T x P', explanation='M + K ΓåÆ M is the brother of K

K % T ΓåÆ K is the father of T

T x P ΓåÆ T is the sister of P

Therefore, K is the father of P and M is the uncle of P' WHERE qno=18;
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'If A + B means A is the mother of B , A - B means A is the brother B, A % B means A is the father of B and A x B means A is the sister of B, which of the following shows that P is the maternal uncle of Q?','Q - N + M x P','P + S x N - Q','P - M + N x Q','Q - S % P','option3','P - M ΓåÆ P is the brother of M

M + N ΓåÆ M is the mother of N

N x Q ΓåÆ N is the sister of Q

Therefore, P is the maternal uncle of Q.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'Nitya points to a icture and says "He is the son of the only son of my mother." How is Nitya related to that boy?','brother','uncle','cousin','father','option4','the boy is the only son of Nitya, hence he is the father',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'If A is the brother of B, B is the sister of C, and C is the father of D, how D is related to A?','brother','sister','grandfather','none of the above','option4','If D is Male, the answer is Nephew.

If D is Female, the answer is Niece.

As the sex of D is not known, hence, the relation between D and A cannot be determined.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'If A + B means A is the brother of B,  A - B means A is the sister of B and A x B means A is the father of B. Which of the following means that C is the son of M?','M - N x C + F','F - C + N x M','N + M - F x C',' M x N - C + F','option4','M x N ΓåÆ M is the father of N

N - C ΓåÆ N is the sister of C

and C + F ΓåÆ C is the brother of F.

Hence, M is the father of C or C is the son of M.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'Introducing a boy, a girl said, "He is the son of the daughter of the father of my uncle." How is the boy related to the girl?','brother','nephew','uncle','son-in-law','option1','The father of the boy''s uncle ΓåÆ the grandfather of the boy and daughter of the grandfather ΓåÆ sister of father.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,' Lata says, "this man in the picture is the son of the only son of my grandfather." How is the man in the photograph related to Lata?','brother','uncle','cousin','nephew','option1','The man in the photograph is the son of the only son of Lata''s grandfather i.e., the man is the son of Lata''s father. Hence, the man is the brother of Lata.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'If A + B means A is the brother of B, A x B means A is the son of B, and A % B means B is the daughter of A then which of the following means M is the maternal uncle of N?','M + O x N','M % O x N + P','M + O % N','none of the above','option4','because the sex of O is unknown',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'If D is the brother of B, how B is related to C? To answer this question which of the statements is/are necessary?1. The son of D is the grandson of C.
2. B is the sister of D.','only 1','only 2','either1or2','1 and 2 both are required','option4','Given: D is the brother of B.

From statement 1, we can detect that D is son of C (son of D is the grandson of C).

From statement 2, we can detect that B is ''Female'' (sister of D).

Therefore, B is daughter of C.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(39,'Bajpai said, "He is the son of the only daughter of the father of my brother." How Bajpai is related to the man in the photograph?','nephew','brother','fathr','maternal uncle','option4','The man in the photo is the son of the sister of Bajpai. Hence, Bajpai is the maternal uncle of the man in the photograph.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(40,'Deepak said to Nitin, "That boy playing with the football is the younger of the two brothers of the daughter of my father''s wife." How is the boy playing football related to Deepak?','son','brother','cousin','brother in law','option2','Father''s wife ΓåÆ mother. Hence, the daughter of the mother means sister and sister''s younger brother means brother. Therefore, the boy is the brother of Deepak.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(41,'Pointing a photograph X said to his friend Y, "She is the only daughter of the father of my mother." How X is related to the person of photograph?','daughter','son','nephew','cannot be decided','option2','The only daughter of the father of X''s mother'' means mother of X.

Hence X is the son of the lady in the photograph.

 "How X is a male?" Here X said to ''his'' --> from the word ''his'' we can find X is Male.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(42,'Veena who is the sister-in-law of Ashok, is the daughter-in-law of Kalyani. Dheeraj is the father of Sudeep who is the only brother of Ashok. How Kalyani is related to Ashok?','mother in law','aunt','wife','mother','option4','Ashok is the only brother of Sudeep and Veena is the sister-in-law of Ashok. Hence Veena is the wife of Sudeep. Kalyani is the mother-in-law of Veena. Kalyani is the mother of Ashok',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(43,' If A + B means A is the sister of B, A x B means A is the wife of B, A % B means A is the father of B and A - B means A is the brother of B. Which of the following means T is the daughter of P?','P x Q % R + S - T','P x Q % R - T + S','P x Q % R + T - S','P x Q % R + S + T','option2','P x Q ΓåÆ P is the wife of Q

Q % R ΓåÆ Q is the father of R

R - T ΓåÆ R is the brother of T

T + S ΓåÆ T is the sister of S.

Therefore, T is the daughter of P.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(44,'Pointing to Divya, Abhijit said, "Her granddaughter is the only daughter of my brother." How is the divya related to Abhijit?','sister','grandmom','mom in law','mother','option4','Daughter of Abhijit''s brother ΓåÆ niece of Abhijit. Thus the granddaughter of the woman is Abhijit''s niece.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(45,'Amit said - "This girl is the wife of the grandson of my mother". How is Amit related to the girl?','brother','grandfather','husband','father in law','option4','The girl is the wife of grandson of Amit''s mother i.e., the girl is the wife of son of Amit. Hence, Amit is the father-in-law of the girl.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(46,'Vibhuti introduces sheenam to his boss as the mother of Shavth who is the only grandchild of his father. How is Sheenam related to Vibhuti?','mother','wife','daughter','granddaughter','option2','sheenam is the mother of Vibhuti''s only chid',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(47,'A and B are children of D. Who is the father of A? To answer this question which of the statements (1) and (2) is necessary?
1. C is the brother of A and the son of E.
2. F is the mother B.','only1','only2','either 1 or 2','both 1 n 2','option2','A and B are children of D.

From (1), C is the brother B and son of E.

Since, the sex of D and E are not known. Hence (1) is not sufficient to answer the question.

From (2). F is the mother of B. Hence, F is also the mother of A. Hence D is the father of A.

Thus, (2) is sufficient to answer the question.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(48,'Arvind was introduced by Ratnika like His mother is the only daughter of my mother. How is the Ratnika related to the Arvind?','sister','daughter','mother','aunt','option3','Only daughter of my mother ΓåÆ myself.',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(49,'If P $ Q means P is the brother of Q, P # Q means P is the mother of Q, P * Q means P is the daughter of Q in A # B $ C * D, who is the father?','D','B','C','A','option1','A is the mother of B, B is the brother of C and C is the daughter of D. Hence, D is the father.
A      (Parents)        D
|                       |
|                       |
B - is - Brother - of - C',NULL,NULL,NULL,NULL);
INSERT INTO blood(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(50,'Introducing Sheenam, Prateek says, She is the wife of only nephew of only brother of my mother. How Sheenam is related to Prateek?','wife','sister','sister in law','girlfriend','option1','Brother of mother means maternal uncle. Hence only nephew of Prateek''s maternal uncle means Prateek himself.',NULL,NULL,NULL,NULL);
UPDATE boats SET que='Speed of a boat in standing water is 9kmph and the speed of the stream is 1.5kmph.
A man rows to a place at a distance of 105 km and comes back to the starting point.
 Find the total time taken by him.
' WHERE qno=12;
UPDATE boats SET que='In one hour, a boat goes 11 km/hr along the stream and 5 km/hr against the stream. The speed of the boat in still water (in km/hr) is?', option1='8 kmph', option2='2 kmph', option3='4 kmph', option4='10 kmph', explanation='Speed in still water
=1/2 (11 + 5) kmph = 8 kmph.' WHERE qno=13;
UPDATE calendar SET que='By how many degrees does the minute hand move in the same time, in which the hour hand move by 28 ? ' WHERE qno=3;
UPDATE calendar SET que='How many degrees will the minute hand move, in the same time in which the second hand move 4800 ?', option1='80', option2='40', option3='60', option4='20', explanation='Minute hand covers 480/60= 80' WHERE qno=7;
UPDATE calendar SET que='2012 January 1st is Sunday, then which day is the Indian Independence day of the same year.', option4='Wednesday', explanation='30+ 29+ 31 + 30 + 31 + 30 + 31+15
= 227/7 = reminder = 3
So Independence day is Wednesday' WHERE qno=18;
UPDATE classification SET answer='option1', explanation='All numbers are perfectly divisible by 3 except 64' WHERE qno=4;
UPDATE classification SET option1='CF2', answer='option3' WHERE qno=9;
UPDATE classification SET explanation='All other numbers are in the form of n^2+4 where n is a natural number' WHERE qno=21;
UPDATE decode SET que='In a certain code, TOGETHER is written as RQEGRJCT. In the same code, PAROLE will be written as?', option1='NCPQJG', option2='RCPQJG', option3='RYPQJG', explanation='The letters at the odd positions are moved two steps backward and the letters at even positions are moved two steps forward to get the value.

So PAROLE will be NCPQJG' WHERE qno=3;
UPDATE direction SET option3='D', answer='option2' WHERE qno=9;
UPDATE general_science SET option4='sodium', answer='option3' WHERE qno=28;
UPDATE idioms SET que='To have an axe to grind', option1='A private end to serve', option2='To fail to arouse interest', option3='To have no result', option4='None of these', answer='option1' WHERE qno=26;
CREATE TABLE "indian_geo" (
        `qno`   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        `que`   TEXT NOT NULL,
        `option1`       TEXT NOT NULL,
        `option2`       TEXT NOT NULL,
        `option3`       TEXT NOT NULL,
        `option4`       TEXT NOT NULL,
        `answer`        TEXT NOT NULL,
        `explanation`   TEXT,
        `attempted`     TEXT,
        `notes` TEXT,
        `time`  TEXT,
        `fav`   TEXT
);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(1,'The percentage of earth surface covered by India is','2.4','3.4','4.4','5.4','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(2,'The present forest area of India, according to satellite data, is','increasing','decreasing','static','decreasing in open forest area but increasing in closed forest area','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(3,'The Indias highest annual rainfall is reported at','Namchi','churu','mawsynram','chamba','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(4,'Which of the following groups of states has the largest deposits of iron ore?','Andhra Pradesh and Karnataka','Bihar and Orrisa','M.P. and Maharashtra','west bengal and assam','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(5,'The refineries are Mathura, Digboi and Panipat are set up by','Indian oil','Hindustan petroleum','bharat petroleum','crude distillation unit','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(6,'What is the predominant type of Indian agriculture?','commercial','extensive','intensive','subsistence','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(7,'The Radcliffe line is a boundary between','india and Pakistan','india and china','india and myanmar','india and afghanistan','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(8,'Which of the following has a potential for harnessing of tidal energy in India?','gulf of cambay','gulf of mannar','backwaters of kerela','chilka lake','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(9,'The typical area of sal forest in the Indian peninsular upland occurs','on wetern ghats','between tapti and narmada','to NE of godavari','on malwa plateau','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(10,'The state having a largest area of forest cover in India is','tamil nadu','haryana','madhya pardesh','assam','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(11,'The year ____ is called a Great Divide in the demographic history of India.','1901','1921','1941','1951','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(12,'The only private sector refinery set up by Reliance Petroleum Ltd. is located at','guwahati','jamnagar','mumbai','chennai','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(13,'The only state in India that produces saffron is','Assam','himachal','j&K','meghalaya','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(14,'Three important rivers of the Indian subcontinent have their sources near the Mansarover Lake in the Great Himalayas. These rivers are','indus,jhelum,sutlej','brahmaputra,sutlej,yamuna','brahmaputra,indus,sutlej','jhelum,sutlej and yamuna','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(15,'The zonal soil type of peninsular India belongs to','red soil','yellow soil','black soil','sandy soil','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(16,'The most plausible explanation for the location of the Thar desert in western India is','the obstruction caused by the Aravalis to the rain-bearing wind that proceeds to the Ganga Valley','the evaporation of moisture by heat','the absence of mountains to the north of Rajasthan to cause orographic rainfall in it','that the moisture carried by the South-west monsoon is driven away by the dry upper air current','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(17,'The northern boundary of the peninsular plateau of Indian runs parallel to the Ganga and the Yamuna from Rajmahal hills to a point near','allahabad','delhi','gwalior','jaipur','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(18,'Which of the following food grain crops occupies the largest part of the cropped area in India?','barley','maize','rice','wheat','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(19,'The number of major languages, recognized in the Indian Union as official language, are','15','22','12','9','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(20,'The oldest rocks in India are reported from','Dharwar region','Aravali range','Vindhyas','Siwalik','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(21,'The Paithan (Jayakwadi) Hydro-electric project, completed with the help of Japan, is on the river','ganga','cauvery','narmada','godavari','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(22,'The percentage of irrigated land in India is about','45','65','35','25','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(23,'The southernmost point of peninsular India, that is, Kanyakumari, is','north of tropic of Cancer','south of equator','south of capricorn','north of equator','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(24,'The pass located at the southern end of the Nilgiri Hills in south India is called','palgha gap','bhorghat pass','thalgat pass','bolan pass','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(25,'Which of the following factors are responsible for the rapid growth of sugar production in south India as compared to north India?
1. Higher per acre field of sugarcane
2. Higher sucrose content of sugarcane
3. Lower labour cost
4. Longer crushing period','1 and 2','1,2,3','1,3,4','1,2,4','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(26,'The principal copper deposits of India lie in which of the following places?','Hazaribag and Singbhum of Bihar','Khetri of rajasthan','anantpur in A.P.','Siwaliks','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(27,'Which of the following union territories of India has the highest density of population per sq km?','pondicherry','lakshadweep','delhi','chandigarh','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(28,'The Salal Project is on the river','jhelum','ravi','sutlej','chenab','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(29,'The Yarlung Zangbo river, in India, is known as','ganga','indus','brahmaputra','mahanadi','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO indian_geo(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(30,'The only zone in the country that produces gold is also rich in iron is','North east','North west','South','North','option3',NULL,NULL,NULL,NULL,NULL);
UPDATE indian_history SET answer='option3' WHERE qno=14;
UPDATE inventions SET option4='William Friese-Greene' WHERE qno=7;
UPDATE inventions SET option1='Willis Carrier', answer='option1' WHERE qno=12;
UPDATE inventions SET explanation=NULL WHERE qno=23;
UPDATE numbers SET answer='option2' WHERE qno=3;
UPDATE numbers SET option2='6534', explanation='121*54=6534' WHERE qno=6;
CREATE TABLE "partnership" (
        `qno`   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        `que`   TEXT NOT NULL,
        `option1`       TEXT NOT NULL,
        `option2`       TEXT NOT NULL,
        `option3`       TEXT NOT NULL,
        `option4`       TEXT NOT NULL,
        `answer`        TEXT NOT NULL,
        `explanation`   TEXT,
        `attempted`     TEXT,
        `notes` TEXT,
        `time`  TEXT,
        `fav`   TEXT
);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(1,'Arjun starts business with Rs. 3500 and after 5 months, Bharat joins with Arjun as his partner. After a year, the profit is divided in the ratio 2:3. What is B''s contribution in the capital','9000','7000','5000','4000','option1','Let B contribution is x.
3500Γêù12 / 7x=2/3

=>14x=126000

=>x=Rs 9000',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(2,'A and B established a firm together. A''s investment was thrice that of B''s. A also kept the investment for twice as much time as B. If B got a profit of 4000, what was the total profit?','4000','24000','28000','none of the above','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(3,'priya riya and supriya invested in the ratio of 2:3:5 in a bussiness.After 3 months priya invested additional 33.3% of her original invesment and riya invested an additional 25% of her original invesment and supriya withdrew 10% of her original invesment.at the end of the year,a total profit of rs 15,390 was earned.find the share of riya in partnership?','15390','4884.25','8448.50','4225.25','option2','Let sum invested by priya riya and supriya is 2x, 3x and 5x.

Now find the total investments of all at the end of the year.

For Priya,
2x * 3 + 2x (400/3)% 9 = 30x.

For Riya,
3x X 3 + 3x (125)% 9 = 39.75x

For Supriya,
5x X 3 + 5x (90)% 9 = 55.50x

Ratio of shares = Ratio of the profits earned.

So, share of riya = 15390 (39.75/125.25)
= 4884.25.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(4,'Anil and Billu started a partnership business investing some amount in the ratio of 3 : 5. Chandan joined them after six months with an amount equal to that of B. In what proportion should the profit at the end of one year be distributed amount Anil, Billu and Chandan','3:7:5','6:10:5','6:10:7','6:7:5','option2','Let initial investment of A is 3x and B is 5x, then C investment is also 5x, but most important to note in this question is the time duration of investment
Like, A invested for 12 months, B invested for 12 months and C invested for 6 months.

A:B:C =(3x*12):(5x*12):(5x*6)
= 36:60:30
= 6:10:5',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(5,'In a joint business, the capital of A, B and C were Rs. 45000, Rs. 30000 and Rs. 22500 respectively. A withdrew half of his capital after 4 months. After 9 months of this withdrawal, there was a total profit of Rs. 28400. Find the share of profit for A.','10027.48','10498.57','7873.93','28400','option1','Ratio of share in profits is:-
(45000 x 4 + 22500 x 9) : 30000 x 13 : 22500 x 13

372500 : 390000 : 292500

149 : 156 : 117

Share of Profit of A,
= 28400 x (149/422)
= 10027.48

Share of Profit of B,
= 28400 x (156/422)
= 10498.57

Share of Profit of C,
= 28400 x (117/422)
= 7873.93.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(6,'priya and anupama started a business by investing Rs 85000 and 15000 respectively. In what ratio the profit earned after 2 years be divided between Priya and Anupama respectively.','17:3','17:1','17:4','17:2','option1','P:Q = 85000:15000 = 17:3

Important to note there that if both have invested for different period of times then we had to multiply with number of months to get the desired ratio.
',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(7,'Kamal started a business investing Rs 9000. After five months, Sameer joined with a capital of Rs 8000. If at the end of the year, they earn a profit of Rs. 6970, then what will be the share of Sameer in the profit ?','2380','2260','2280','2300','option1','Now as per question, Kamal invested for 12 months and Sameer invested for 7 months.
So
Kamal:Sameer = (9000*12):(8000*7)
= 108:56
= 27:14

Sameer Ratio in profit will be
=(6970Γêù1441)=Rs 2380',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(8,'Arun, Kamal and Vinay invested Rs. 8000, Rs. 4000 and Rs. 8000 respectively in a business. Arun left after six months. If after eight months, there was a gain of Rs. 4005, then what will be the share of Kamal?','870','890','880','900','option2','Arun : Kamal : Vinay = (8,000 x 6) : (4,000 x 8) : (8,000 x 8)
= 48 : 32 : 64
= 3 : 2 : 4

Kamal''s share=4005Γêù29=Rs 890',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(9,'A and B entered into a partnership investing Rs. 16000 and Rs. 12000 respectively. After 3 months, A withdrew Rs. 5000 while B invested Rs. 5000 more. After 3 more months C joins the business with a capital of Rs. 21000. The share of B exceeds that of C, out of a total profit of Rs. 26400 after one year by','3200','3400','3600','3800','option3','A:B: C = 16000*3 + 11000*9:12000*3 + 17000*9:21000*6
= 147:189:126 = 7:9:6
Difference of B and CΓÇÖs shares =
Rs. [26400 * (9/22) ΓÇö 26400 * (6/22))
= Rs. 3600',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(10,'Three partners shared the profit in a business in the ratio 4:6:8. They had partnered for 12 months, 8 months and 6 months respectively. What was the ratio of their investments?','4:6:8','4:7:8','4:8:16','4:9:16','option4','Let their investments be Rs. x for 12 months, Rs. y for 8 months and Rs. z for 6 months respectively.

Then, 12x : 8y : 6z = 4 : 6 : 8

Now, 12x/8y = 4/6
   9x=4y
  y=9x/4

And, 12x/6z = 4/8
  4x=z
   z=4x

Therefore, x : y: z  =  x : 9x/4: 4x
 =  4 : 9 : 16',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(11,'P and Q started a business investing Rs, 85,000 and Rs. 15,000 respectively. In what ratio the profit named after 2 years bo divided between P and Q respectively ?','17:23','17:3','17:33','3:4','option2','P : Q = 85000 : 15000 = 86 : 15 = 17 : 3 .',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(12,'Rs. 700 is divided among A, B, C so that A receives half as much as B and B half as much as C. Then C''s share is','200','300','400','500','option3','Let C = x.
Then B = x/2
and A = x/4

A:B:C = 1:2:4.
C''s share Rs.[(4/7)*700) = 400',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(13,'A  and B start a business jointly. A invests Rs. 16,000 for 8 months and B remains in  the business for 4 months, Out of total profit, B claims - of the profit. How much money was contributed by B ?','12000','12800','13000','14500','option2','Let the total profit be Rs. x. Then, B = 2x/7 and A = (x - 2x /7) =  5x / 7.
So, A : B = 5x/7 : 2x/7 = 5 : 2.
Let B s capital be Rs. y.
 Then, (16000 * 8) / ( y * 4) = 5/2  <=> (16000 * 8 * 2) / (5 * 4)
 = 12800..  ',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(14,'Three partners A,B and C shared the profit in a software business in the ratio 5:7:8. They had partnered for 14 months, 8 months and 7 months respectively. Find the ratio of their investments?','19:49:64','20:49:64','20:49:65','20:50:64','option2','Let the total profit be Rs. x. Then, B = 2x/7 and A = (x - 2x /7) =  5x / 7.
So, A : B = 5x/7 : 2x/7 = 5 : 2.
Let B s capital be Rs. y.
 Then, (16000 * 8) / ( y * 4) = 5/2  <=> (16000 * 8 * 2) / (5 * 4)
 = 12800',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(15,'Reena and Shaloo are partners in a business, Reena invests Rs, 35,000 for 8 months and Shaloo invests Rs. 42,000 for 10 months, out of a profit of  Rs. 31,570, Reena''s share is :','12628','18245','11235','10253','option1','Ratio of their shares = (35000 * 8) : (42000 * 10) = 2 : 3.
Reena''s share Rs. 31570 * (2 / 5) = Rs. 12628.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(16,'A starts business with Rs. 3500 and after 5 months, B joins with A as his partner. After a year, the profit is divided in the ratio 2 : 3. What is B''s contribution in the capital?','7500','8000','8500','9000','option4','Let B''s capital be Rs. x.
Then, (3500*12)/7x
= 2/3
=> 14x = 126000
=> x = 9000.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(17,'In a business, A and C invested amounts in the ratio 2:1, whereas the ratio between amounts invested by A and B was 3:2. If Rs. 1,57,300 was their profit, how much amount did B receive ?','48400','54200','64000','74000','option1','A : B = 3 : 2 => B : A = 2 : 3 = 4 : 6 and A : C = 2 : 1 = 6 : 3.
So, B : A : C = 4 : 6 : 3 or A : B : C = 6 : 4 : 3.
B''s share = Rs. (157300 x 4/13 ) = Rs. 48400.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(18,'A and B started a business jointly A''s investment was thrice the investment of B and the period of his investment was two times the period of investment of B. If B received Rs. 4000 as profit, then their total profit is :','22000','28000','32000','36000','option2','Suppose B invested Rs. x for y months. Then, A invested Rs. 3x for 2y months.
So, A : B = (3x * 2y) : (x * y) = 6xy : xy = 6 : 1.
B''s profit : Total profit  = 1 : 7.
Let the total profit be Rs. x Then,  1/7 = 4000/x  or x = 28000.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(19,'If 4 (A''s capital) = 6 (B''s capital) = 10 (C''s capital), then out of a profit of Rs. 4650, C will receive ____','700','800','900','1000','option3','Let 4A = 6B = 1OC = k. Then, A = k/4, B = k/6,  and C =k/10 .

A : B :C = k/4 : k/6 : k/10 = 15 : 10 : 6.

Hence, C''s share (4650 *  6/31) = Rs, 900.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(20,'A is a working and B is sleeping partners in a business. A puts in Rs. 5000 and B puts in Rs.6000. A receives % of the profit for managing the business and the rest is divided in proportion to their capital. What does B get out of a profit of Rs. 880?','560','273','420','350','option3','Total profit = Rs. 880

A''s share for managing the business i.e =Rs.110

Remaining profit of A and B as per their capital = 880 - 110 = Rs. 770

Ratio of amounts = 5000 : 6000 = 5 : 6

Sum of ratios = 5 + 6 = 11

A''s share = \inline 770\times \frac{5}{11}= Rs.350

A''s total share = 350 + 110 = Rs. 460

B''s share = \inline 770\times \frac{6}{11}=Rs.420',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(21,'A is a working and B is sleeping partners in a business. A puts in Rs. 5000 and B puts in Rs.6000. A receives 12.5% of the profit for managing the business and the rest is divided in proportion to their capital. What does B get out of a profit of Rs. 880?','10500,13500,19500','10500,13500,18500','10500,13500,17500','10500,13500,16500','option4','Total profit = Rs. 880

A''s share for managing the business i.e =Rs.110

Remaining profit of A and B as per their capital = 880 - 110 = Rs. 770

Ratio of amounts = 5000 : 6000 = 5 : 6

Sum of ratios = 5 + 6 = 11

A''s share = 770*(5/11)= Rs.350

A''s total share = 350 + 110 = Rs. 460

B''s share =  770*(6/11)=Rs.420',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(22,'A, B and C enter into a partnership and their shares are in the ratio 1/2 : 1/3 : 1/4. After 2 months, A withdraws half of his capital and after 10 months, a profit of Rs. 378 is divided among them. What is B''s share ?','144','169','225','339','option1','ratio of initial investments = 1/2 : 1/3 : 1/4 = 6 : 4 : 3.
Let their initial investments be 6x, 2x and 3x respectively.
A : B : C = (6x * 2 + 3x * 10) : (4x * 12) : (3x * 12) = 42 : 48 : 36 = 7 : 8 : 6.
B''s share =  Rs. (378 * 8/21) = Rs. 144.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(23,'Two partners investede Rs. 1250 and Rs. 850 respectively in a business. They distributed 60% of the profit equally and decide to distribute the remaining 40% as the ratio of their capitals. If one partner received Rs. 30 more than the other, find the total profit?','850','455.75','393.75','373.75','option3','Let the total profit be Rs.x

60% of the profit = 3x/5

from  this part of the profit each gets = Rs.3x/10

40% of the profit = 2x/5

Now, this amount has been divided
in the ratio of capitals 1250 : 850 = 25 :17

therefore Share on first capital =Rs.5x/21

Share on second capital = 17x/105

Total money received by 1st investor = 113x/210

Total money received by 2nd investor = 97x/210

therefore x = 393.75

Hence total profit = Rs. 393.75',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(24,' P and Q started a business investing Rs 85000 and Rs 15000 resp. In what ratio the profit earned after 2 years be divided between P and Q respectively.','17:5','17:3','17:6','17:7','option2','In this type of question as time frame for both investors is equal then just get the ratio of their investments.

P:Q = 85000:15000
= 85:15
= 17:3',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(25,'A began a business with Rs. 85,000. He was joined afterwards by B with Ks. 42,500. For how much period does B join, if the profits at the end of the year are divided in the ratio of 3 : 1 ?','4 months','5 months','6 months','8 months','option4','Suppose B joined for x months . Then,  ( 85000 * 12 )/(42500 * x) = 3. or x = (85000 * 12) / (42500 * 3) = 8.
So, B joined for 8 months.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(26,'Shreemanth and Devika started a business investing Rs.22,500 and Rs.35,000 respectively. Out of a total profit of
Rs. 13,800. Devika''s share is','9600','8500','8450','8400','option4','Suppose B joined for x months .
Then,  ( 85000 * 12 )/(42500 * x) = 3.
or x = (85000 * 12) / (42500 * 3) = 8.
So, B joined for 8 months.',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(27,' A and B invest in a business in the ratio 3 : 2. If 5% of the total profit goes to charity and A''s share is Rs. 855, the total profit is :','500','1000','1500','2000','option3','Ratio of their shares= 22500 : 35000
=9 : 14

Devika''s share= Rs.(13800*14/23)
= Rs. 8400',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(28,'A starts a business with a capital of 18000. B&C joined 4 months later with a capital of 12000. After8 months all of them withdrew half of their capital .What will be the ratio of their profit after the end of 10 months?    ','1:2:3','27:1:1','1:26:1','2:27:1','option2','Ratio of Profit,
18000 x 8 + 9000 x 2 : 12000 x 4 + 6000 x 2 : 12000 x 4 + 6000 x 2

162000 : 60000 : 60000

27 : 1 : 1',NULL,NULL,NULL,NULL);
INSERT INTO partnership(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(29,'Check which among following are required to answer this questions :

Three friends started a businesss, let there names are A, B and C. What profit B will get, if,

1. C invested Rs. 8000 for nine months, his profit was 3/2 times that of B''s and his investment was four times that of A.

2. A and B invested for one year in the proportion 1 : 2 respectively.

3. The three together got Rs. 1000 as profit at the year end.','only 1 and 3','only 1 and 2','all 1, 2, 3','none','option3','We will require all the statements to answer this question,

1 and 2 will give :
C = Rs. (8000 x 9) for 1 month = Rs. 72000 for 1 month.
A = Rs. (1/4 * 8000 * 12 ) for 1 month = Rs 24000 for 1 month
B = Rs 48000 for one month

C:A:B = 72000 : 24000 : 48000 = 3 : 1 : 2

From 3, we will get total Profit = 1000

Now from the ratio and total profit we can get Share of C.

C share will be = 1000 * 2/6 = 333 (1/3)',NULL,NULL,NULL,NULL);
UPDATE percentage SET que='In an election between two candidates, one got 55% of the total valid votes, 20% of the votes were invalid. If the total number of votes was 7500, the number of valid votes that the other candidate got, was', option1='2700', option2='2000', option3='1350', option4='1800', explanation='Number of valid votes = 80% of 7500 = 6000.

 Valid votes polled by other candidate = 45% of 6000

= (45/100) x 6000 = 2700.
' WHERE qno=5;
UPDATE percentage SET option4='23 1/13 %', explanation='y=100   x=130
130---------30
100---------? => 23 1/13%' WHERE qno=6;
UPDATE permutation SET que='In how many ways can the letters of the word LEADER be arranged?', option1='312', option2='360', option3='240', option4='144', explanation='The word LEADER contains 6 letters, namely 1L, 2E, 1A, 1D and 1R.

Required number of ways
= 6! / (1!)(2!)(1!)(1!)(1!)
=360
' WHERE qno=21;
UPDATE permutation SET option1='120' WHERE qno=22;
UPDATE permutation SET que='How many 3-digit numbers can be formed from the digits 2, 3, 5, 6, 7 and 9, which are divisible by 5 and none of the digits is repeated?', option1='17', option2='16', option3='20', option4='38', explanation='Since each desired number is divisible by 5, so we must have 5 at the unit place. So, there is 1 way of doing it.

The tens place can now be filled by any of the remaining 5 digits (2, 3, 6, 7, 9). So, there are 5 ways of filling the tens place.

The hundreds place can now be filled by any of the remaining 4 digits. So, there are 4 ways of filling it.

 Required number of numbers = (1 x 5 x 4) = 20' WHERE qno=24;
UPDATE permutation SET que='In how many different ways can the letters of the word OPTICAL be arranged so that the vowels always come together?', option1='360', option2='720', option3='252', explanation='The word OPTICAL contains 7 different letters.

When the vowels OIA are always together, they can be supposed to form one letter.

Then, we have to arrange the letters PTCL (OIA).

Now, 5 letters can be arranged in 5! = 120 ways.

The vowels (OIA) can be arranged among themselves in 3! = 6 ways.

 Required number of ways = (120 x 6) = 720' WHERE qno=26;
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'How many three digit numbers with distinct digits can be formed such that the product of the digits is the cube of the positive integer?','21','24','36','30','option4','total 30 different 3 digit numbers can be formed',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'One-half of Heather''s age two years from now plus one-third of her age three years ago is twenty years. How old is she now?','120','24','26','20','option2','Age now:  H
age two years from now:  H + 2
age three years ago:  H - 3

Now I need certain fractions of these ages:

one-half of age two years from now:  ( 1/2 )(H + 2) = H/2 + 1
one-third of age three years ago:  ( 1/3 )(H - 3) = H/3 - 1

The sum of these two numbers is twenty, so I''ll add them and set this equal to 20:

H/2 + 1 + H/3 - 1 = 20
H/2 + H/3 = 20
3H + 2H = 120
5H = 120
H = 24

Heather is 24 years old.',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'How many 6 digit numbers contain exactly 4 different digits?','4536','294840','191520','none','option2','Let the four different digits be a, b, c, d, then the number can be of two forms i.e. aabbcd or aaabcd.

So to calculate the total numbers, first we need to find the number of ways to select 4 digits (which is C(10, 4)), then number of ways to select the digits to be repeated in each case (which is C(4,2) and C(4,1) respectively). And then the number of ways of arrangement in both the cases (which is 6!/(2!)2 and 6!/3! respectively).

Hence the number of ways to form such 6-digit numbers become = C(10, 4)[C(4, 2)6!/(2!)2 + C(4, 1)6!/3!] = 10*9*8*7*5*13 = 327600 .

But is this the final answer? .....certainly not

See in the initial selection for the digits, we have used all 10 digits (including 0) for selecting the four distinct digits. That means in the above arrangements there will be some numbers which''d be starting with zero. Now thinking alternately, first place of the number can be filled with any 10 digits equally. So removing the cases starting with zero, we are left with (9/10) of previously calculated result.

Thus the answer will be 9*9*8*7*5*13 = 294840',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'The total number of integral solutions for (x,y,z) such that xyz = 24 ,is','36','90','120','none','option3','You are to just write 24 as a product of three integers (i - all three positive, ii - any two(i.e. selected in 3C2 = 3 ways) negative). So if I count the number of ways to write 24 as product of three natural numbers, that needs to be multiplied with 4 to get the final answer.',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'let A be the set of 4-digit number "a1a2a3a4"  where a1>a2>a3>a4
then how many values of A are possible ?','126','84','210','none','option3','We are to select 4 different digits out of available 10 and see that they can be arranged only in one possible way satisfying the given condition.',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,'conference attended by 200 delegates is held in a hall. The hall has 7 doors,marked A,B,......,G. At each door,an entry book is kept and the delegates entering through that door sign in it in the order in which they enter . If each delegate is free to enter any time and through any door he likes, how many different sets of seven lists would arise in all ? (Assume that every person signs only at his first entry)','206C6','199P5','199C5','206P6','option4','206!/6!',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'How many subsets of (1,2,3,4,5,6,7) contain 6 as the largest number?','32','31','64','128','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'A palindromic number reads the same as forward as backwards (12121) How many 6 digit palindromic numbers are there?','810','900','729','1000','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(39,'A bag has 5 balls in it with a number written on it. one ball has number 2 on it and one has number 5 on it, one has no.7 and 2 balls have number 8 on them. The balls are taken out of the bag without repalcement and placed in the order they are drawn from left to right to create a 5 digit number. how many of these numbers are not prime?','21','0','47','60','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(40,'In Kerela, the rickshaw license plates are issued with the following ptter: each plate has 3 letter followed by 3 digits (0-9). the letters can be in any combination. however each digit must be eqal or greater than the preceding one. how many different plates can the state issue? ','26^3*9^3','26^3*90','26^3*120','26^3*10','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(41,'Think about all the 5 digit numbers that can be formed using digits 1,3,5,7 and 9. If these numbers are arranged in an increasing order, the 500th number is? ','17599','11799','17999','19131','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(42,'A person has 6 friends to be invited for dinner through invitation cards, and he has 3 servants. In how many ways can he extend the invitation card?','729','700','900','850','option1','We can see that the 1st friend has 3 options to receive the card, i.e. either from 1st servant or 2nd or 3rd. Similarly 2nd friend also has 3 options to receive the card, i.e. either from 1st servant or 2nd or 3rd. So we can say that each of the 6 friends has 3 options to receive the card. Hence the answer would be 3 * 3 * 3 * 3 * 3 * 3  = 729 ways',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(43,'There are 10 questions in an exam. In how many ways can a person attempt at least one question?','1203','1023','1302','1230','option2','A person can attempt 1 question or 2 questions or .....till all 10 questions. One question out of ten questions can be attempted in 10C1 = 10 ways. Similarly two questions out of ten questions can be attempted in 10C2 = 45 ways. Going ahead by the same logic, all ten questions can be attempted in 10C10 = 1 way. Hence the total number of ways = 10 + 45 + 120 +.....10 + 1 = 1023 ways (Using the formula of Combination).

Alternate Method:

Or some logic can be applied: Every question has 2 options, either it is attempted or not. Going ahead with this logic, since there are 10 questions, and each question has 2 options, so total number of cases = 210= 1024. But this count includes one case in which no question is attempted. This is the violation of the information given. So this case needs to be subtracted. Hence the total number of cases would be 1024 - 1 = 1023.',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(44,'How many permutations of 3 different digits are there, chosen from the ten digits 0 to 9 inclusive?

(Such as drawing ten numbered marbles from a bag, without replacement)','84','504','720','890','option3','The number of permutations of 3 digits chosen from 10 is 10P3
= 10!/(10 - 3)!
= 10 * 9 * 8
= 720',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(45,'How many permutations of 4 different letters are there, chosen from the twenty six letters of the alphabet?','14950','358800','23751','67543','option2','The number of permutations of 4 letters chosen from 26 is 26P4 = 26 * 25 * 24 * 23 = 358,800',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(46,'how many committes of 5 ppl can be chosen from 10 ppl?','252','30240','2002','100000','option1','In choosing a committee, order does not matter, so we need the number of combinations of 5 people chosen from 10
= 10C5
= 10!/(5!)(5!)
= (10 * 9 * 8 * 7 * 6)/(5 * 4 * 3 * 2 * 1)
= 30,240/120
= 252',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(47,'Jones is the Chairman of a committee. In how many ways can a committee of 5 be chosen from 10 people given that Jones must be one of them?','126','252','495','132','option1','ones is already chosen, so we need to choose another 4 from 9.
In choosing a committee, order doesn''t matter, so we need the number of combinations of 4 people chosen from 9
= 9C4
= 9!/(4!)(5!)
= (9 * 8 * 7 * 6)/(4 * 3 * 2 * 1)
= 3,024/24
= 126',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(48,'A special type of password consists of four different letters of the alphabet, where each letter is used only once. How many different possible passwords are there?','4^26','14950','358800','385580','option3','The number of permutations of 4 letters chosen from 26 is 26P4 = 26 * 25 * 24 * 23 = 358,800',NULL,NULL,NULL,NULL);
INSERT INTO permutation(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(49,'A password consists of two letters of the alphabet followed by three digits chosen from 0 to 9. Repeats are allowed. How many different possible passwords are there?','492804','676000','650000','766000','option2','The number of ways of choosing the letters = 26 * 26 = 676
The number of ways of choosing the digits = 10 * 10 * 10 = 1,000

So the number of possible passwords = 676 * 1,000 = 676,000',NULL,NULL,NULL,NULL);
UPDATE pipes SET option3='36 min' WHERE qno=18;
CREATE TABLE "politics" (
        `qno`   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        `que`   TEXT NOT NULL,
        `option1`       TEXT NOT NULL,
        `option2`       TEXT NOT NULL,
        `option3`       TEXT NOT NULL,
        `option4`       TEXT NOT NULL,
        `answer`        TEXT NOT NULL,
        `explanation`   TEXT,
        `attempted`     TEXT,
        `notes` TEXT,
        `time`  TEXT,
        `fav`   TEXT
);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(1,'The president can assign any of the functions of the union government to the state government','in consultation with CJI','in consultaion with the state governor','in his discretion','in consultation with the state govt.','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(2,'The president can assign any of the functions of the union government to the state government','chief minister','governor','chief justice','vice president','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(3,'The new committee system constitutes an improvement over the earlier committee system in so far as','it assures representation to all the political parties in proportion to their strength in the Parliament','it enables the Parliament to examine the grants of all the ministries and departments in detail','it enables the Parliament to accept the demands of various ministries without scrutiny','nonne','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(4,'The president can be removed from his office before the expiry of his normal term only on the recommendation of','the Supreme Court','The CJI','council of ministers','the 2 houses of parliament','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(5,'The Objectives Resolution was unanimously adopted by the Constituent Assembly on','1 oct 1948','26 Dec 1946','22 jan 1947','26 nov 1946','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(6,'The preamble declares India as a sovereign state which implies that India is free to conduct her ','internal affairs only','external affairs only','both internal and external affairs','none of the above','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(7,'The name of the Laccadive, Minicoy and Amindivi islands was changed to Lakshadweep by an Act of Parliament in','1970','1971','1972','1973','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(8,'The minimum age required to become the prime minister of India is','25','30','40','35','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(9,'The objective of the Morley-Minto Reforms was','extension of provincial assemblies','o give more powers to local government','to abolish the post of secretary of the state for India',' establish diarchy in provinces','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(10,'The oath of office is conducted to the president by','the CJI','the vice president','the prwsident','the prime minister','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(11,'he members of the state legislative assemblies are elected for a period of','1 year','5 years','10 years','7 years','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(12,'the PMO is','creaed by parliament','created by constituion','none of the above','both 1. and 2.','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(13,'The parliament works through committees which','help the govt in formulation of policies','xercise effective control over government on a regular and continuing basis','ensure that the parliament strictly adheres to the provision of the constitution and the prescribed parliamentary procedure','none ','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(14,'the muslim league as a political party was founded in:','1906','1909','1915','1919','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(15,'The members of the state legislature exercise control over the council of the ministers through','questions and supplementary questions','criticism of its policies','adjournment motion','all of the above','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(16,'The preamble to our constitution includes all the following except','adult franchise','equality of status','fraternity','justice','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(17,'The members of the committees of Parliament are','appointed by the speaker or elected by the House from amongst its own members','nominated by the leaders of the various parties in the Parliament','appointed by the speaker or elected by the House from amongst persons who are not members of Parliament','nominated by the prime minister','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(18,'The parliament can legislate on the subject in the state list','f the President issues an order authorizing it to do so','if the Supreme Court of India gives authority to the Parliament of India in this regard
','if the Rajya Sabha passes a resolution by two-third of its to legislate on a state matter in the national interest','none','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(19,'The president convenes and prorogues all sessions of Parliament in consultation with','the PM','the speaker','the leader of opposition','none','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(20,'The national flag was adopted by the Constituent Assembly of India on 22 July 1947 and was presented to the nation at the midnight session of the Assembly on 14th August 1947 on behalf of','the minorites of india','the NIC','the women of india','the people of india','option3','The National Flag of India was adopted by the Constituent Assembly on the 22nd July, 1947 and presented to the Nation on behalf of the women of India at the mid-night session of the assembly on August 14, 1947.',NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(21,'The preamble to our constitution provided that India is','a sovereign, socialist and democratic republic','a sovereign republic with a socialist pattern of society','a socialist, secular and democratic republic',' sovereign, socialist, secular and democratic republic','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(22,'The minimum age required to become a member of Rajya Sabha is','21','25','30','35','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(23,'The president can be impeached for','for not taking the prime minister''s advice','disregarding parliament','violation of constitution','all of the above','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(24,'The name of the union given in the Constitution is','India/Bharat','Hindustan/India','Bharatvarsha/India','Bharatvarsha/hindustan','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(25,'The phrase ''procedure established by the law'' means','judges in India can declare a law invalid simply because in their opinion the law is not due or is unjust','judges in India can question the fairness or validity of an undue law even if it is within the limits of the constitution','judges in India cannot question the fairness or validity of a law, provided it is within the limits of the constitution','none of the above','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(26,'the national anthem was written by','Bankim Chandra Chatterjee','sarojini naidu','rabindranath tagore','florance nightengale','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(27,'The member of a State Public Service Commission can be removed on the ground of misbehavior only after an enquiry has been conducted by the','high court ','supreme court','state athority','none','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(28,'The members of the standing committee are taken from the Lok Sabha and Rajya Sabha in the ratio of','4:1','2:1','3:1','1:1','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(29,'The preamble enshrines certain ideals that were first spelt out in','the Objectives Resolution adopted by the Constituent Assembly','a resolution adopted at the Karachi session of the Indian National Congress','the speech by Jawaharlal Nehru on the banks of Ravi when he called for Purana Swaraj','nehru report','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO politics(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(30,'The position of the president which was undermined by the 42nd amendment was sub-sequently somewhat retrieved by the','44 amendment','45 amendment','26 amendment','none','option1',NULL,NULL,NULL,NULL,NULL);
UPDATE preposition SET que='He complains ....... headache.', option4='Off', answer='option3' WHERE qno=1;
UPDATE preposition SET option3='Off' WHERE qno=2;
UPDATE preposition SET option4='Away' WHERE qno=17;
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'The ratio of cost price and selling price is 4:5. The profit percentage is?','10%','20','25','30','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'If the selling price of an article is 4/3 of its cost price, the profit percentage is?','16 2/3%','25.5%','20 1/2%','33 1/3%','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'salesman sells  one book for Rs. 840 at  a gain of 20%   and another for Rs. 960 at a loss of 4%.  His total gain or loss percentage is?','5  15/17% gain','5  15/17% loss','6  2/3% gain','6  2/3% loss','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'The ratio between SP and CP of an article is 7:5. What is the ratio between the profit and the cost price of the article','2:7','5:2','7:2','5:7','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'A man gain 20% by selling an article for certain price. If he sells it at double the price the percentage of profit will be?','40','100','120','140','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,'At what profit percentage an article must be sold so that by selling at half that price,  there may be a loss of 30%','25','36','40','42','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'Sheela buys a second hand handset for rs. 4700 and spends rs. 800  on its screenguard and repairs. If she sells it for Rs. 5800 , what is her gain percentage?','4  4/7','5  5/11','10','12','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'The cost price of 20 computers is the same as the selling price of ΓÇÿxΓÇÖ  computers. If the profit is 25% , find the value of x.','15','16','18','25','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(39,'At Croma, the profit is 320% of the cost. If the selling price remains constant and the cost increases by 25%, what is the approximate profit  percentage of the SP??','30','70','100','250','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(40,'Archana sold An Ipad  for Rs. 18700 and incurred a loss of 15%. At what price must she sell the Ipad inorder to make a profit of 15%.','21000','22500','25300','25800','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(41,'Vipin incurred a loss equivalent to cost price of 5 nailcutters on selling 17 of them at 720.  The cost price of each nailcutter would be :','45','50','55','60','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(42,'The Maximum Retail Price (MRP) of a product is 55% above its manufacturing cost. The product is sold through a retailer, who earns 23% profit on his purchase price. What is the profit percentage (expressed in nearest integer) for the manufacturer who sells his product to the retailer? The retailer gives 10% discount on MRP.','31','22','15','13.4','option4','In Profit Loss questions where all data is given in percentage terms, assume cost price to be 100.
  Let the manufacturing cost = 100

The MRP of the product is 55% above its manufacturing cost
  The MRP of the product = 100 + 55% of 100 = 155.

The retailer sells the product after offering a discount of 10% on the MRP
  So, the retailer sells the product at 155 - 10% of 155 = 155 - 15.5 = 139.5

The retailer makes a 23% profit on his purchase price
Let the purchase price for the retailer be x.
  So, the retailer sells the product at x + 23% of x = 123% of x.

Step to retailer sells the product @ 139.5 = 123% of x
1.23x = 139.5
(or) x = 139.5/1.23
Therefore, x = 113.4

x is the purchase price for the retailer.
So, x has to be selling price for the manufacturer.
The manufacturer sold the product at 113.4.

Cost to the manufacturer is 100.
So, profit made by the manufacturer is 13.4.
Because we assumed the cost price to be 100, the manufacturer makes a 13.4% profit.',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(43,'A merchant buys two articles for Rs.600. He sells one of them at a profit of 22% and the other at a loss of 8% and makes no profit or loss in the end. What is the selling price of the article that he sold at a loss?','404.8','440','536.8','160','option1','Let C1 be the cost price of the first article and C2 be the cost price of the second article.
Let the first article be sold at a profit of 22%, while the second one be sold at a loss of 8%.

We know, C1 + C2 = 600.
The first article was sold at a profit of 22%. Therefore, the selling price of the first article
= C1 + (22/100)C1 = 1.22C1
The second article was sold at a loss of 8%.

Therefore, the selling price of the second article
= C2 - (8/100)C2 = 0.92C2.

The total selling price of the first and second article = 1.22C1 + 0.92C2.

As the merchant did not make any profit or loss in the entire transaction, his combined selling price of article 1 and 2 is the same as the cost price of article 1 and 2.

Therefore, 1.22C1 + 0.92C2 = C1+C2 = 600
As C1 + C2 = 600, C2 = 600 - C1.
Substituting this in 1.22C1 + 0.92C2 = 600, we get
1.22C1 + 0.92(600 - C1) = 600
or 1.22C1 - 0.92C1 = 600 - 0.92*600
or 0.3C1 = 0.08*600 = 48
or C1 = 48/(0.3) = 160.
If C1 = 160, then C2 = 600 - 160 = 440.
The item that is sold at loss is article 2. The selling price of article 2 = 0.92*C2 = 0.92*440 = 404.80.',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(44,'Two merchants sell, each an article for Rs.1000. If Merchant A computes his profit on cost price, while Merchant B computes his profit on selling price, they end up making profits of 25% respectively. By how much is the profit made by Merchant B greater than that of Merchant A?','66.67','50','125','200','option2','Merchant B computes his profit as a percentage of selling price. He makes a profit of 25% on selling price of Rs.1000.
i.e. his profit = 25% of 1000 = Rs.250

Merchant A computes his profit as a percentage of cost price.
Therefore, when he makes a profit of 25% or 1/4th of his cost price, then his profit expressed as a percentage of selling price = 1/(1+4) = 1/5th or 20% of selling price.
So, Merchant A makes a profit of 20% of Rs.1000 = Rs.200.

Merchant B makes a profit of Rs.250 and Merchant A makes a profit of Rs.200
Hence, Merchant B makes Rs.50 more profit than Merchant A.',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(45,'Asif marks his goods up by 75% above his cost price. What is the maximum % discount that he can offer so that he ends up selling at no profit or loss?','75','46.67','300','42.85','option4','Let us assume that the cost price of the article = Rs.100
Therefore, the merchant would have marked it to Rs.100 + 75% of Rs.100 = 100 + 75 = 175.

Now, if he sells it at no profit or loss, he sells it at the cost price.
i.e. he offers a discount of Rs.75 on his selling price of Rs.175

Therefore, his % discount =  = 42.85%',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(46,'If  Parvathi offers a discount of 30% on the list price, then she makes a loss of 16%. What % profit or % loss will she make if she sells at a discount of 10% of the list price?','6% loss','0.8% profit','6.25% loss','8% profit','option4','Let the cost price of the article be Rs.100.
Let the List price of the article by "x".

Then, when the merchant offers a discount of 30%, the merchant will sell the article at x - 30% of x = 70% of x = 0.7x. .......(1)
Note: Discount is measured as a percentage of list price.

The loss made by the merchant when she offers a discount of 30% is 16%.
Therefore, the merchant would have got 100 - 16% of 100 = Rs.84 when she offered a discount of 30%. .......(2)
Note: Loss is always measured as a percentage of cost price.

Therefore, equating equations (1) and (2), we get
0.7x = 84
or x = 120.

If the list price is Rs.120 (our assumption of cost price is Rs.100), then when the merchant offers a discount of 10%, she will sell the article at
120 - 10%o of 120 = Rs.108.

As the cost price of the article was Rs.100 and the merchant gets Rs.108 while offering a discount of 10%, she will make a profit of 8%.',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(47,'Three siblings Akbar, Salim, Anwar divide $1105 amongs them in such a way that if $10, $20 and $15 are removed from the sums that Alkbar, Salim and Anwar received respectively, then the share of the sums that they got will be in the ratio of 11 : 18 : 24. How much did Anwar receive?','495','510','480','375','option1','Let Akbar=A, Salim=B, Anwar=C

Let the amount of money received by A, B and C be x, y and z respectively.
Sum of money with the three of them, x + y + z = 1105.
It is mentioned that the money that they possess are in the ratio 11:18:24, after removing $10, $20 and $15 respectively.
Then x - 10 : y - 20 : z -15 will be in the ratio 11 : 18 : 24

So, x - 10 = 11k, y - 20 = 18k and z - 15 = 24k
Adding the money left with the three of them after removing $10, $20 and $15 respectively, we get
x - 10 + y - 20 + z - 15 = 11k + 18k + 24k
Or x + y + z - 10 - 20 - 15 = 53k
Or x + y + z - 45 = 53k
We know x + y + z = 1105.
So, 1105 - 45 = 53k
1060 = 53k
or k = 20.

To compute the amount that C received, substitute value of k in ratio of z.

We know that z - 15 = 24k
z - 15 = 24 * 20 = 480

Therefore, z = 480 + 15 = $495
Anwar received 495$',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(48,'Mary and Mike enter into a partnership by investing $700 and $300 respectively. At the end of one year, they divided their profits such that a third of the profit is divided equally for the efforts they have put into the business and the remaining amount of profit is divided in the ratio of the investments they made in the business. If Mary received $800 more than Mike did, what was the profit made by their business in that year?','2000','6000','4000','3000','option4','Because a third of the profit gets divided equally between the two partners for their efforts, assuming the profit made during the year to be $6x will make calculations easy.

One third of the profit = 2x
Therefore, $2x would have been shared equally and the remaining $4x would have been shared in the ratio 7 : 3.

i.e., 70% of 4x would go to Mary and 30% of 4x would go to Mike.

MaryΓÇÖs share = 0.70 * 4x
MikeΓÇÖs share = 0.30 * 4x.

Difference between the two = 4x (0.7 - 0.3)
= 4x * (0.4) = 1.6x

The difference in the amount that Mary received that year is only account of the difference in component of the profit shared in the ratio of their investments.

i.e., The difference in share of profit made in the ratio of their investments, 1.6x = 800
x = 801.6801.6 = 500

Hence, the profit made by the company during the year = $6x = 6 * 500 = $3000.',NULL,NULL,NULL,NULL);
INSERT INTO profit(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(49,'In an election contested by two parties, Party D secured 12% of the total votes more than Party R. If party R got 132,000 votes, by how many votes did it lose the election?','240000','300000','168000','36000','option4','Let the percentage of the total votes secured by Party D be x%
Therefore, the percentage of total votes secured by Party R = (x - 12)%

As there are only two parties contesting in the election, the sum of the votes secured by the two parties should add up to 100%

i.e., x + x - 12 = 100
2x - 12 = 100
or 2x = 112 or x = 56%.

If Party D got 56% of the total votes, then Party R would have got (56 - 12) = 44% of the total votes.

44% of the total votes = 132,000
i.e., 44/100 * T = 132,000
=> T = 132000Γêù100/44 = 300,000 votes.

The margin by which Party R lost the election = 12% of the total votes
= 12% of 300,000 = 36,000.',NULL,NULL,NULL,NULL);
UPDATE ratio SET que='Mother divided the money among Ron, Sam and Maria in the ratio 2 : 3 : 5. If Maria got $150, find the total amount of money?' WHERE qno=18;
UPDATE ratio SET answer='option2' WHERE qno=20;
UPDATE sentence_arrange SET option2='QSPR' WHERE qno=2;
UPDATE sentence_arrange SET option4='RSPQ', answer='option2' WHERE qno=5;
UPDATE sentence_complete SET answer='option3' WHERE qno=10;
UPDATE spelling SET answer='option3' WHERE qno=3;
UPDATE spelling SET answer='option2' WHERE qno=4;
UPDATE spelling SET option2='Aprehension', answer='option1' WHERE qno=23;
UPDATE sports SET option1='Lawn Tennis' WHERE qno=5;
UPDATE sqlite_sequence SET seq=40 WHERE rowid=12;
UPDATE sqlite_sequence SET seq=50 WHERE rowid=18;
UPDATE sqlite_sequence SET seq=40 WHERE rowid=20;
UPDATE sqlite_sequence SET seq=50 WHERE rowid=21;
UPDATE sqlite_sequence SET seq=50 WHERE rowid=22;
UPDATE sqlite_sequence SET seq=50 WHERE rowid=28;

UPDATE substitute SET answer='option4' WHERE qno=8;
UPDATE substitute SET option2='Hole' WHERE qno=20;
UPDATE synonyms SET option1='Impressive', answer='option1' WHERE qno=30;
UPDATE time_and_distance SET option3='50 min', explanation='Time taken to cover 60 km = 30 minutes

Distance covered in in 1 min=60/30=2 km

Time taken to cover 100 km
=100/2=50 min' WHERE qno=29;
UPDATE time_and_work SET option3='10/7 days', explanation='Time taken by Ram to do the work = 2 days

Work done by Ram in 1 day = 1/2

Time taken by Shyam to do the work = 5 days

Work done by Shyam in 1 day = 1/5

So, work by Ram and Shyam in one day
= 1/2 + 1/5
= (5 + 2)/10
= 7/10

Therefore, Ram and Shyam can do a piece of work in 10/7 days.' WHERE qno=1;
UPDATE time_and_work SET explanation='Let the work be completed in X days
(X-3)/24 + X/36 + 4/48 = 1

multiply both sides of the equation by 72
3(x-3)+2x+6=72
3x-9+2x+6=72
5x-3=72
5x=75
x=15

Hence, work will be completed in 15 Days' WHERE qno=6;
UPDATE time_and_work SET que='A does half as much work as B in three-fourth of the time. If together they take 18 days to complete the work, how much time shall B take to do it?', option2='30 days', explanation='Suppose B takes x d├íys to do the work.
As per question A will take

2 Γêù 3/4 Γêù x=3x/2 days
2Γêù34Γêùx=3x2days

(A+B)s 1 days work= 1/18
1/x + 2/3x = 1/18 or x = 30 days' WHERE qno=9;
UPDATE time_and_work SET que='A is thrice as good a workman as B and takes 10 days less to do a piece of work than B takes. B alone can do the whole work in?', option1='11 days', option2='12 days', option3='10 days', option4='15 days' WHERE qno=24;
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(31,'A can do a work in 15 days and B in 20 days. If they work on it together for 4 days, then the fraction of the work that is left is :','1/4','1/10','7/15','8/25','option1','A''s 1 day''s work =      1/15
B''s 1 day''s work =    1/20
(A + B)''s 1 day''s work = 1/15 +       1/20 = 7/60
(A + B)''s 4 day''s work =7/60 * 4      .

Therefore, Remaining work = 1 - (7/60)*4 = 8',NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(32,'A can lay railway track between two given stations in 16 days and B can do the same job in 12 days. With help of C, they did the job in 4 days only. Then, C alone can do the job in:','9  1/5 days','9  2/5 days','9  3/5 days','10','option3','(A + B + C)''s 1 day''s work =1/4
A''s 1 day''s work =1/16
B''s 1 day''s work =1/12

C''s 1 day''s work =1/4 - (1/16 + 1/12) = 7/48

So, C alone can do the work in= 9 3/5 days.
',NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(33,'A, B and C can do a piece of work in 20, 30 and 60 days respectively. In how many days can A do the work if he is assisted by B and C on every third day?','12','15','16','18','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(34,'A is thrice as good as workman as B and therefore is able to finish a job in 60 days less than B. Working together, they can do it in:','20 days','22.5 days','25 days','30 days','option2','Ratio of times taken by A and B = 1 : 3.
The time difference is (3 - 1) 2 days while B take 3 days and A takes 1 day.
If difference of time is 2 days, B takes 3 days.
If difference of time is 60 days,
B takes 3x 60= 90 days.

So, A takes 30 days to do the work.
A and B together can do the work in 30*90/30+90 =22.5
',NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(35,'A alone can do a piece of work in 6 days and B alone in 8 days. A and B undertook to do it for Rs. 3200. With the help of C, they completed the work in 3 days. How much is to be paid to C?','375','400','600','800','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(36,'If 6 men and 8 boys can do a piece of work in 10 days while 26 men and 48 boys can do the same in 2 days, the time taken by 15 men and 20 boys in doing the same type of work will be:','4 days','5 days','6 days','7 days','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(37,'A can do a piece of work in 4 hours, B and C together can do it in 3 hours, while A and C together can do it in 2 hours. How long will B alone take to do it?','8 hrs','10 hrs','12 hrs','24 hrs','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(38,'A can do a certain work in the same time in which B and C together can do it. If A and B together could do it in 10 days and C alone in 50 days, then B alone could do it in:','15 days','20 days','25 days','30 days','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(39,'A does 80% of a work in 20 days. He then calls in B and they together finish the remaining work in 3 days. How long B alone would take to do the whole work?','23 days','37 days','37.5 days','40 days','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(40,'A machine P can print one lakh books in 8 hours, machine Q can print the same number of books in 10 hours while machine R can print them in 12 hours. All the machines are started at 9 A.M. while machine P is closed at 11 A.M. and the remaining two machines complete work. Approximately at what time will the work (to print one lakh books) be finished ?','11:30 AM','12 noon','12:30 pm','1:00 pm','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(41,'A can finish a work in 18 days and B can do the same work in 15 days. B worked for 10 days and left the job. In how many days, A alone can finish the remaining work?','5','5.5','6','8','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(42,'4 men and 6 women can complete a work in 8 days, while 3 men and 7 women can complete it in 10 days. In how many days will 10 women complete it?','35','40','45','50','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(43,'A and B can together finish a work 30 days. They worked together for 20 days and then B left. After another 20 days, A finished the remaining work. In how many days A alone can finish the work?','40','50','54','60','option4',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(44,'P can complete a work in 12 days working 8 hours a day. Q can complete the same work in 8 days working 10 hours a day. If both P and Q work together, working 8 hours a day, in how many days can they complete the work?','5  5/11','5  6/11','6  5/11','6  6/11','option1',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(45,'10 women can complete a work in 7 days and 10 children take 14 days to complete the work. How many days will 5 women and 10 children take to complete the work?','3','5','7','cant be determined','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(46,'X and Y can do a piece of work in 20 days and 12 days respectively. X started the work alone and then after 4 days Y joined him till the completion of the work. How long did the work last?','6','10','15','20','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(47,'A is 30% more efficient than B. How much time will they, working together, take to complete a job which A alone could have done in 23 days?','11 days','13 days','20  3/17 days','none of these','option2',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(48,'Ravi and Kumar are working on an assignment. Ravi takes 6 hours to type 32 pages on a computer, while Kumar takes 5 hours to type 40 pages. How much time will they take, working together on two different computers to type an assignment of 110 pages?','7 hours 30 mins','8 hours','8 hours 15 mins','8 hours 25 mins','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(49,'A, B and C can complete a piece of work in 24, 6 and 12 days respectively. Working together, they will complete the same work in:','1/24 day','7/24 day','3 3/7 days','4 days','option3',NULL,NULL,NULL,NULL,NULL);
INSERT INTO time_and_work(qno,que,option1,option2,option3,option4,answer,explanation,attempted,notes,time,fav) VALUES(50,'Sakshi can do a piece of work in 20 days. Tanya is 25% more efficient than Sakshi. The number of days taken by Tanya to do the same piece of work is:','15','16','18','25','option2','Ratio of times taken by Sakshi and Tanya
= 125 : 100 = 5 : 4.
Suppose Tanya takes x days to do the work.
5 : 4 :: 20 : x
5/4=20/x
x = 16 days.
Hence, Tanya takes 16 days to complete the work.
',NULL,NULL,NULL,NULL);

