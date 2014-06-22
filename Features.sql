--Tore
Select neu.Name, sum(neu.Tore)
From (
SELECT V.Name, sum(S.Tore_Heim) as Tore 
FROM Verein V,Spiel S
WHERE (V.V_ID = S.Heim ) AND S.Spieltag IN (
Select distinct Spieltag FROM Spiel
Order by Spieltag desc 
FETCH FIRST 3 ROWS ONLY)
Group By V.Name

Union all

SELECT V.Name, sum(S.Tore_Gast) 
FROM Verein V,Spiel S
WHERE (V.V_ID = S.Gast ) AND S.Spieltag IN (
Select distinct Spieltag FROM Spiel
Order by Spieltag desc 
FETCH FIRST 3 ROWS ONLY)
Group By V.Name

) neu
Group by neu.Name

-- Gegentore
Select neu.Name, sum(neu.Tore)
From (
	SELECT V.Name, sum(S.Tore_Gast) as Tore 
	FROM Verein V,Spiel S
	WHERE (V.V_ID = S.Heim ) AND S.Datum IN (
	Select distinct Datum FROM Spiel
	Order by Datum desc 
	FETCH FIRST 3 ROWS ONLY)
	Group By V.Name

	Union all

	SELECT V.Name, sum(S.Tore_Heim) 
	FROM Verein V,Spiel S
	WHERE (V.V_ID = S.Gast ) AND S.Datum IN (
	Select distinct Datum FROM Spiel
	Order by Datum desc 
	FETCH FIRST 3 ROWS ONLY)
	Group By V.Name
) neu
Group by neu.Name

-- Anzahl der Niederlagen
Select neu.Name, sum(neu.Niederlagen)
From (
	SELECT V.Name, count(*) as Niederlagen 
	FROM Verein V,Spiel S
	WHERE (V.V_ID = S.Heim ) 
	AND (S.Tore_Heim < S.Tore_Gast)  
	AND S.Datum IN (
	Select distinct Datum FROM Spiel
	Order by Datum desc 
	FETCH FIRST 5 ROWS ONLY)
	Group By V.Name

	Union all

	SELECT V.Name, count(*) 
	FROM Verein V,Spiel S
	WHERE (V.V_ID = S.Gast ) 
	AND (S.Tore_Heim > S.Tore_Gast)
	AND S.Datum IN (
	Select distinct Datum FROM Spiel
	Order by Datum desc 
	FETCH FIRST 5 ROWS ONLY)
	Group By V.Name
) neu
Group by neu.Name


-- nur zum checken
SELECT V.Name, S.Gast,S.Heim,S.Datum,S.Tore_Heim, S.Tore_Gast 
FROM Verein V,Spiel S
WHERE ((V.V_ID = S.Heim )  OR (V.V_ID = S.Gast ))

AND S.Datum IN (
Select distinct Datum FROM Spiel
Order by Datum desc 
FETCH FIRST 5 ROWS ONLY)
order By V.Name
