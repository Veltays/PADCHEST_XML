(: 1) Récupérer toutes les <Images> :)
let $all-images := /Images/image

(: 2) Filtrer les images avec "loc right" :)
let $filtered :=
  for $image in $all-images
  let $locs := $image/Localizations/Localization
  where $locs = "loc right"
  return $image

(: 3) Filtrer les images avec "loc right" :)
return count($filtered)