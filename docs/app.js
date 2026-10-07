let META={topics:[],mocks:[],total:0}, cur={list:[],i:0,sec:0,t0:0,tick:null};
const $=id=>document.getElementById(id);
const ansIndex=a=>({option1:0,option2:1,option3:2,option4:3}[a]??-1);
const fmt=s=>String(Math.floor(s/60)).padStart(2,'0')+':'+String(s%60).padStart(2,'0');
function startTimer(){stopTimer();cur.t0=Date.now();cur.tick=setInterval(()=>{$('q-timer').textContent=fmt(Math.floor((Date.now()-cur.t0)/1000))},500)}
function stopTimer(){if(cur.tick)clearInterval(cur.tick)}
async function init(){
 const r=await fetch('data/topics.json');META=await r.json();
 $('st-q').textContent=META.total.toLocaleString();$('st-t').textContent=META.topics.length;
 const secs=['All','Quantitative Aptitude','Logical Reasoning','Verbal Ability','General Knowledge'];
 $('section-tabs').innerHTML=secs.map((s,i)=>`<button class="${i==0?'on':''}" data-s="${s}">${s}</button>`).join('');
 $('section-tabs').onclick=e=>{if(e.target.dataset.s){[...$('section-tabs').children].forEach(b=>b.classList.toggle('on',b===e.target));renderTopics(e.target.dataset.s);}};
 renderTopics('All');renderMocks();initFormulas();initTips();
 $('search').addEventListener('input',onSearch);
 $('q-prev').onclick=()=>showQ(cur.i-1);$('q-next').onclick=()=>showQ(cur.i+1);
 $('quiz-back').onclick=()=>{$('quiz').hidden=true;stopTimer()};
}
function renderTopics(sec){
 const list=META.topics.filter(t=>sec==='All'||t.section===sec);
 $('topic-grid').innerHTML=list.map(t=>`<div class="card" data-t="${t.table}"><span class="n">${t.count}</span><b>${t.name}</b><small>${t.section}</small></div>`).join('');
 $('topic-grid').onclick=async e=>{const c=e.target.closest('.card');if(!c)return;await openQuiz(c.dataset.t)};
}
function renderMocks(){
 $('mock-grid').innerHTML=META.mocks.map(m=>`<div class="card" data-t="${m.table}"><span class="n">${m.count}</span><b>${m.name}</b><small>Exam simulation · 25 Qs</small></div>`).join('');
 $('mock-grid').onclick=async e=>{const c=e.target.closest('.card');if(!c)return;await openQuiz(c.dataset.t)};
}
async function openQuiz(table){
 const meta=[...META.topics,...META.mocks].find(t=>t.table===table);
 const r=await fetch(`data/questions/${table}.json`);cur.list=await r.json();cur.i=0;
 $('quiz-title').textContent=`${meta.name} · ${cur.list.length} questions`;
 $('quiz').hidden=false;$('quiz').scrollIntoView({behavior:'smooth'});startTimer();showQ(0);
}
function showQ(i){
 if(i<0||i>=cur.list.length)return;cur.i=i;startTimer();
 const q=cur.list[i],ai=ansIndex(q.a);
 $('q-pos').textContent=`${i+1} / ${cur.list.length}`;
 $('q-progress').style.width=((i+1)/cur.list.length*100)+'%';
 $('q-card').innerHTML=`<p><b>Q${i+1}.</b> ${esc(q.q)}</p>`+q.o.map((o,k)=>o?`<button class="opt" data-k="${k}">${'ABCD'[k]}. ${esc(o)}</button>`:'').join('')+`<div id="exp"></div>`;
 $('q-card').onclick=e=>{
  const b=e.target.closest('.opt');if(!b||$('q-card').dataset.done)return;$('q-card').dataset.done='1';
  const k=+b.dataset.k;const btns=[...document.querySelectorAll('.opt')];
  btns.forEach(x=>x.disabled=true);
  if(k===ai)b.classList.add('correct');else{b.classList.add('wrong');if(btns[ai])btns[ai].classList.add('correct');}
  if(q.e)$('exp').innerHTML=`<div class="exp"><b>Explanation:</b> ${esc(q.e)}</div>`;
 };
 delete $('q-card').dataset.done;
}
let searchIdx=null;
async function onSearch(e){
 const q=e.target.value.trim().toLowerCase();const box=$('search-results');
 if(q.length<2){box.hidden=true;return}
 if(!searchIdx){ // build light index: topic names + first fetch of small files on demand is heavy; search topics first, questions lazily across cached files
  searchIdx=true;
 }
 const topicHits=META.topics.filter(t=>(t.name+' '+t.section).toLowerCase().includes(q)).slice(0,12);
 // search within already-cached question files would need fetch-all; limit to topic hits + hint
 box.hidden=false;
 box.innerHTML=`<p class="tiny">${topicHits.length} topic matches — open a topic to practice, or keep typing a topic name.</p>`+topicHits.map(t=>`<div class="hit"><b>${t.name}</b> <small>· ${t.section} · ${t.count} Qs</small> <a href="#" data-t="${t.table}">Open →</a></div>`).join('');
 box.onclick=ev=>{const a=ev.target.closest('a[data-t]');if(!a)return;ev.preventDefault();openQuiz(a.dataset.t)};
}
async function initFormulas(){
 const r=await fetch('data/formulas.json');const F=await r.json();
 const tabs=Object.keys(F);$('formula-tabs').innerHTML=tabs.map((t,i)=>`<button class="${i==0?'on':''}" data-t="${t}">${t.replace(/_/g,' ')}</button>`).join('');
 const draw=t=>{const d=F[t];if(!d||!d.rows){$('formula-list').innerHTML='<p>No data</p>';return}
  $('formula-list').innerHTML=d.rows.slice(0,60).map(row=>`<div class="fc">${row.filter(Boolean).map((c,i)=>i==0?`<b>${esc(c)}</b> `:esc(c)).join('<br>')}</div>`).join('')+ (d.rows.length>60?`<p class="tiny">Showing 60 of ${d.rows.length}</p>`:'');};
 draw(tabs[0]);$('formula-tabs').onclick=e=>{if(!e.target.dataset.t)return;[...$('formula-tabs').children].forEach(b=>b.classList.toggle('on',b===e.target));draw(e.target.dataset.t)};
}
async function initTips(){
 const r=await fetch('data/interview.json');const T=await r.json();
 const tabs=Object.keys(T);$('tip-tabs').innerHTML=tabs.map((t,i)=>`<button class="${i==0?'on':''}" data-t="${t}">${t}</button>`).join('');
 const draw=t=>{const d=T[t];if(!d||!d.rows){$('tip-list').innerHTML='<p>No data</p>';return}
  $('tip-list').innerHTML=d.rows.slice(0,40).map(row=>`<div class="fc">${row.filter(Boolean).map((c,i)=>i==0?`<b>${esc(c)}</b> `:esc(c)).join('<br>')}</div>`).join('')+(d.rows.length>40?`<p class="tiny">Showing 40 of ${d.rows.length}</p>`:'');};
 draw(tabs[0]);$('tip-tabs').onclick=e=>{if(!e.target.dataset.t)return;[...$('tip-tabs').children].forEach(b=>b.classList.toggle('on',b===e.target));draw(e.target.dataset.t)};
}
function esc(s){return String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]))}
init();
