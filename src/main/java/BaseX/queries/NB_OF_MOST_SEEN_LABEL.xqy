(: 1) Récupérer toutes les <Images> :)
let $all-images := /Images/image

let $all-labels :=
  for $image in $all-images
  let $labels := $image/Labels/Label/text()
  return $labels

(: 2) Trouver les valeurs distinctes :)
let $unique-labels := distinct-values($all-labels)

(: 3) Calculer la fréquence de chaque label :)
let $frequencies :=
  for $unique in $unique-labels
  let $count :=
    count(
      for $label in $all-labels
      where $label = $unique
      return $label
    )
  order by $count descending
  return
    <label>
      <name>{$unique}</name>
      <count>{$count}</count>
    </label>

(: 4) Garder seulement les 10 premiers :)
return subsequence($frequencies, 1, 10)