#!/bin/bash
export CVS_RSH=ssh
export CVSROOT=:ext:ist199187@sigma.ist.utl.pt:/afs/ist.utl.pt/groups/leic-po/po21/cvs/086
cvs update
while true; do
    read -p "Tudo correto? y/n " yn
    case $yn in
        [Yy]* ) read -p "Escreve a mensagem associada ao commit: " m; cvs commit -m "${m}"; break;;
        [Nn]* ) echo "Corrige ou elimina os ficheiros errados";exit;;
    esac
done
