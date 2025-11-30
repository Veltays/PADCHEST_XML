        let $res :=
          for $label in /Images/image/Labels/Label/text()
          group by $name := $label
          let $count := count($label)
          order by $count descending
          return
            <label>
              <name>{$name}</name>
              <count>{$count}</count>
            </label>

        return subsequence($res, 1, 10)