# Vlak DOS
Mandatory project from subject Programming 4 which was later summited as yearly project and was graded A. Assignment is in Slovak language.

## Zadanie  
Hra je jedným z klonov typického hada, teda pohybujete sa po obrazovke a zbierate predmety, ktoré zväčšujú vašu dĺžku. Akurát v tejto hre sa neobjavujú objekty po jednom na náhodnom mieste, ale sú vždy fixne na tých istých miestach v konkrétnom leveli. Okrem toho každý level obsahuje aj východ - t.j. miesto, kde hra končí, ak sa tam vlak dostane a pozbieral po ceste všetky predmety. Tým sa teda hra stáva skôr logickou ako náhodnou.

Vzhľadom na to, že hry na spôsob hada sa programujú často, pohráme sa v tomto projekte trochu viac aj s vizuálom - bude na vás, či hru spravíte na retro spôsob, alebo jej vdýchnete nový moderný život, podstatné je, aby hra vyzerala dobre.

V prípade, že sa rozhodnete pre túto tému a nie je vám niečo jasné, odporúčam pozrieť link vyššie, prípadne nejaký gameplay alebo mi môžte napísať.

Bodovanie:

[2 body] - Hlavné menu s možnosťami Spusti hru/Ukonči hru

[2 body] - Vykreslenie obrazovky levelu s pomocnými informáciami v spodnej alebo vrchnej časti obrazovky (číslo levelu, skóre) a načítanie levelu zo súboru (formát je na vás)

[3 body] - Vykreslenie vlaku a jeho vágonov v prípade zobratia objektu, vykreslenie objektov, ktoré môže vlak zobrať, vykreslenie prekážok a stien

[3 body] - Správny beh hry (updatuje sa každych 0.x sekundy, pri zobratí objektu sa na vlak napojí nový vagón, v prípade nárazu do steny alebo prejdenia sa správne ukončí level)

[2 body] - Zvuky pre pohyb vlaku, zobratie predmetu, náraz to steny a koniec levelu

[1 bod] - Viac levelov, ktoré sa líšia v štruktúre (aspoň tri, pričom sa predpokladá gradácia obtiažnosti)

[1 bod] - Level možno ľahko resetovať stlačením klávesy R

[1 bod] - Bonusová funkcionalita, viď nižšie

Ako bonusová funkcionalita sa myslí dodatok, ktorý obohacuje hru v nejakom zmysle. Fantázii sa medze nekladú, vymyslieť môžte hocičo hodné bodu. Príklady takýchto funkcionalít:

- objekty nie sú všetky rovnaké, ale aspoň tri rôzne druhy (pričom po zobratí sa prejavia ako iný typ vagónu)
- kedykoľvek počas hry sa po stlačení klávesu F4 zastaví hra, aby užívateľ mohol napísať tzv. heslo - heslo je 5 znakový reťazec, ktorý sa môže viazať k určitému levelu, ak je heslo správne, hra sa presunie do daného levelu
- hlavné menu obsahuje aj možnosť Výber levelu, v ktorom sa dá vybrať level, do ktorého sa hráč pri tomto behu aplikácie už dostal (do menu sa dá dostať kedykoľvek stlačením klávesy Escape)
- iné funkcionality hodné bodov

Z bonusových funkcionalít sa ráta len jedna do bodu, ktorý garantuje, ale v prípade, že stratíte či nezískate nejaký bod v inej z úloh, môžte získať ešte jeden bod za ďalšiu funkcionalitu navyše (maximálne ale stále 15).

Vypracované úlohy a bonusové funkcionality popíšte do súboru readme.txt
