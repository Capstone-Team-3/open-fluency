<table class="summarytable">
	<tbody>
        <g:set var="level" value='["","hard","medium","easy"]'></g:set>
        <g:set var="colors" value='["none","danger","warning","success"]'></g:set>
        <thead>
        <g:each in="${level}">
            <th> ${it} </th>
        </g:each>
        </thead>

        <tr>
        <td>Meaning</td>
        <g:each in="${summaries['Meaning']}">&nbsp;
             <td class=${colors[it[0]]}>
             <g:formatNumber number="${it[1]}" type="number" maxFractionDigits="2" format="#" groupingUsed="false"/>%
             </td>
        </g:each>
        </tr>
        <tr>
        <td>Symbol</td>
        <g:each in="${summaries['Symbol']}">&nbsp;
              <td class=${colors[it[0]]}>
              <g:formatNumber number="${it[1]}" type="number" maxFractionDigits="2" format="#" groupingUsed="false"/>%
              </td>
        </g:each>
        </tr>
        <tr>
        <td>Pronunciation</td>
        <g:each in="${pronunciationSummary}">&nbsp;
            ${it[0]}
        </g:each>
        </tr>
	</tbody>
</table>
