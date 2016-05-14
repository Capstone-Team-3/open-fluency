<g:set var="colors" value='["none","danger","warning","success"]'></g:set>
<tr>
<td>${label}</td>
<g:each in="${summaryList}">&nbsp;
    <td>
     <g:formatNumber number="${it}" type="number" maxFractionDigits="2" format="#" groupingUsed="false"/>%
     </td>
</g:each>
</tr>
